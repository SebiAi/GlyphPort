# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile



# We check what route (aka.) screen is currently shown by checking if the route name
# and the qualified class name of the route object match => we don't want name obfuscation
-keep,allowoptimization,allowshrinking class com.sebiai.glyphport.navigation.routes.*

# JNI is finicky because it uses string lookups, better not optimize
-keep class com.sebiai.glyphport.utils.OpusMetadataUtil