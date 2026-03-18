#include <jni.h>
#include <android/log.h>

// TagLib: Ogg/Opus specific includes
#include <ogg/opus/opusfile.h>
#include <ogg/xiphcomment.h>

#define LOG_TAG "OpusMetadataUtilNative"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jboolean JNICALL
Java_com_sebiai_glyphport_utils_OpusMetadataUtil_writeOpusMetadata(
        JNIEnv* env,
        jobject /* this */,
        jstring path,
        jobjectArray keys,
        jobjectArray values) {

    const char *nativePath = env->GetStringUTFChars(path, nullptr);

    // Use TagLib::Ogg::Opus::File explicitly for Opus files
    TagLib::Ogg::Opus::File file(nativePath);

    if (!file.isValid()) {
        LOGE("Failed to open file: %s", nativePath);
        env->ReleaseStringUTFChars(path, nativePath);
        return false;
    }

    TagLib::Ogg::XiphComment *tag = file.tag();
    if (!tag) {
        LOGE("File has no XiphComment tag: %s", nativePath);
        env->ReleaseStringUTFChars(path, nativePath);
        return false;
    }

    jsize count = env->GetArrayLength(keys);
    for (jsize i = 0; i < count; i++) {
        auto jKey = (jstring) env->GetObjectArrayElement(keys, i);
        auto jVal = (jstring) env->GetObjectArrayElement(values, i);

        const char *cKey = env->GetStringUTFChars(jKey, nullptr);
        const char *cVal = env->GetStringUTFChars(jVal, nullptr);

        TagLib::String key(cKey, TagLib::String::UTF8);
        TagLib::String val(cVal, TagLib::String::UTF8);

        // Replace existing field with same name (true = replace)
        tag->addField(key, val, true);

        env->ReleaseStringUTFChars(jKey, cKey);
        env->ReleaseStringUTFChars(jVal, cVal);

        // Delete explicitly instead of JNI garbage collected to preserve
        // the 16 slots for local references.
        env->DeleteLocalRef(jKey);
        env->DeleteLocalRef(jVal);
    }

    bool success = file.save();
    
    env->ReleaseStringUTFChars(path, nativePath);
    return success;
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_sebiai_glyphport_utils_OpusMetadataUtil_readOpusMetadata(
        JNIEnv* env,
        jobject /* this */,
        jstring path) {

    const char *nativePath = env->GetStringUTFChars(path, nullptr);

    // Use TagLib::Ogg::Opus::File explicitly
    TagLib::Ogg::Opus::File file(nativePath);

    if (!file.isValid()) {
        LOGE("Failed to open file for reading: %s", nativePath);
        env->ReleaseStringUTFChars(path, nativePath);
        return nullptr;
    }

    TagLib::Ogg::XiphComment *tag = file.tag();
    if (!tag) {
        LOGE("File has no XiphComment tag: %s", nativePath);
        env->ReleaseStringUTFChars(path, nativePath);
        return nullptr;
    }

    // Create HashMap
    jclass mapClass = env->FindClass("java/util/HashMap");
    if (mapClass == nullptr) {
        env->ReleaseStringUTFChars(path, nativePath);
        return nullptr;
    }

    jmethodID init = env->GetMethodID(mapClass, "<init>", "()V");
    jobject hashMap = env->NewObject(mapClass, init);
    jmethodID put = env->GetMethodID(mapClass, "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");

    // XiphComment specific: fieldListMap() returns Map<String, StringList>
    const TagLib::Ogg::FieldListMap& fieldMap = tag->fieldListMap();

    for (const auto & it : fieldMap) {
        TagLib::String key = it.first;
        TagLib::StringList values = it.second;
        
        if (values.isEmpty()) continue;

        // Join multiple values with "; " if necessary
        TagLib::String combined;
        // Check if toString with separator is available, typically yes for StringList
        // If not, just take first or iterate. Assuming standard TagLib has toString(separator)
        // Usually: toString(const String &separator = " ") const;
        combined = values.toString(";"); // Use semicolon separator

        // Convert to UTF-8 C string
        const char* keyUtf8 = key.toCString(true); // true = UTF-8
        const char* valUtf8 = combined.toCString(true);

        jstring jKey = env->NewStringUTF(keyUtf8);
        jstring jVal = env->NewStringUTF(valUtf8);

        env->CallObjectMethod(hashMap, put, jKey, jVal);

        // Delete explicitly instead of JNI garbage collected to preserve
        // the 16 slots for local references.
        env->DeleteLocalRef(jKey);
        env->DeleteLocalRef(jVal);
    }
    
    env->ReleaseStringUTFChars(path, nativePath);
    return hashMap;
}
