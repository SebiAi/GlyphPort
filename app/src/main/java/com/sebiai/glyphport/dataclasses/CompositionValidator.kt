package com.sebiai.glyphport.dataclasses

import android.content.Context
import android.net.Uri
import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.arthenica.ffmpegkit.FFprobeKit
import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.safeHandleFFmpegKitSession
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class ValidationResult {
    data class Success(val composition: Composition): ValidationResult()
    data class Failure(val reason: ValidationError): ValidationResult()
}
sealed class ValidationError(val msg: String) {
    class IO(): ValidationError("File reading error")
    class Extension(): ValidationError("Invalid extension - only '.ogg' files are supported")
    class MissingMetadata(): ValidationError("Invalid codec or required light data not found")
    class InvalidMetadata(specifics: String): ValidationError("Invalid metadata: $specifics")
    class CompositionFormatTooNew(version: UInt): ValidationError("Composition format to new: $version")
    data class UnknownOrUnsupportedPhoneModel(val build: String): ValidationError("Unknown or unsupported phone model: $build")
}

private class CompositionImpl(
    override val uri: Uri,
    override val metadata: DecodedCompositionMetadata,
    override val lightData: CompositionLightData,
    override val phoneModel: PhoneModel
) : Composition

class CompositionValidator {
    companion object {
        private const val TAG = "CompositionValidator"

        suspend fun validate(context: Context, uri: Uri, ioDispatcher: CoroutineDispatcher = Dispatchers.IO): ValidationResult = withContext(ioDispatcher) {
            val encodedMetadata: EncodedCompositionMetadata
            try {
                // Test extension - this works because the mime type is determined via the file extension
                if (context.contentResolver.getType(uri) != "audio/ogg")
                    return@withContext ValidationResult.Failure(ValidationError.Extension())

                // Retrieve metadata
                encodedMetadata = getMetadata(context, uri)
                // By delaying the metadata empty check after the composer tag check
                // it is possible to account for the v0 and v1 version of the
                // composition format
            } catch (_: Exception) {
                return@withContext ValidationResult.Failure(ValidationError.IO())
            }

            // Check composer tag
            val composerMatch = Regex("""(?:v(\d+)-)?(\w+) Glyph Composer""").matchEntire(encodedMetadata.composer)
            if (composerMatch == null)
                return@withContext ValidationResult.Failure(ValidationError.InvalidMetadata("Non matching composer"))
            composerMatch.groups[1]?.let {
                val version = it.value.toUInt()
                if (version > 1u)
                    return@withContext ValidationResult.Failure(ValidationError.CompositionFormatTooNew(version))
            }

            // Begin of inserted metadata empty check
            if (encodedMetadata.anyFieldEmpty() && composerMatch.groups[1] != null || // v1 format
                encodedMetadata.anyFieldExceptCustom2Empty() && composerMatch.groups[1] == null) // v0 format - custom2 is missing
                return@withContext ValidationResult.Failure(ValidationError.MissingMetadata())
            // End of inserted metadata empty check

            val phoneModel = PhoneModel.entries.firstOrNull { it.build == composerMatch.groups[2]!!.value }
            if (phoneModel == null)
                return@withContext ValidationResult.Failure(ValidationError.UnknownOrUnsupportedPhoneModel(composerMatch.groups[2]!!.value))

            // Decode
            val decodedMetadata: DecodedCompositionMetadata
            try {
                decodedMetadata = encodedMetadata.decode()
            } catch (e: EncodedCompositionMetadata.DecodeException) {
                return@withContext ValidationResult.Failure(ValidationError.InvalidMetadata(e.message!!))
            }

            // Check light data
            val rawLightData: List<List<Short>>
            try {
                rawLightData = parseLightData(decodedMetadata.author)
            } catch (_: NumberFormatException) {
                return@withContext ValidationResult.Failure(ValidationError.InvalidMetadata("Converting brightness values of light data failed"))
            }

            val lightData: CompositionLightData
            try {
                lightData = CompositionLightData(rawLightData)
            } catch (_: CompositionLightData.EmptyLightDataException) {
                return@withContext ValidationResult.Failure(ValidationError.InvalidMetadata("Empty light data"))
            } catch (_: CompositionLightData.InconsistentDataLength) {
                return@withContext ValidationResult.Failure(ValidationError.InvalidMetadata("Different lengths for lines in light data"))
            }

            if (lightData.columns.toUInt() !in phoneModel.supportedZones)
                return@withContext ValidationResult.Failure(ValidationError.InvalidMetadata("Light data size does not match with phone model. Got: ${lightData.columns.toUInt()}, Expected: ${phoneModel.supportedZones}"))

            return@withContext ValidationResult.Success(CompositionImpl(uri, decodedMetadata, lightData, phoneModel))
        }

        private fun getMetadata(context: Context, uri: Uri): EncodedCompositionMetadata {
            var result = EncodedCompositionMetadata()

            val filePath = FFmpegKitConfig.getSafParameterForRead(context, uri)
            safeHandleFFmpegKitSession(
                session = FFprobeKit.getMediaInformation(filePath),
                onSuccess = { session ->
                    val mediaInformation = session.mediaInformation
                    val streams = mediaInformation.streams
                    if (streams.size > 1) Log.w(TAG, "More than one stream in composition, using first opus stream")

                    streams.firstOrNull { it.codec == "opus" }?.let { stream ->
                        val tags = stream.tags
                        result = EncodedCompositionMetadata(
                            title = tags.optString("TITLE", ""),
                            album = tags.optString("ALBUM", ""),
                            composer = tags.optString("COMPOSER", ""),
                            author = tags.optString("AUTHOR", ""),
                            custom1 = tags.optString("CUSTOM1", ""),
                            custom2 = tags.optString("CUSTOM2", "")
                        )
                    }
                }
            )
            return result
        }

        /**
         * @throws NumberFormatException If a value in the light data can not be converted to a UInt.
         */
        private fun parseLightData(lightData: String): List<List<Short>> {
            val lightDataLines = lightData.replace("\r\n", "\n").split('\n')
            val transformedLightData = lightDataLines
                .map { it.trim().removeSuffix(",") }
                .filter { it.isNotEmpty() }
                .map { it.split(',') }
                .map { it.map { brightness -> brightness.toShort() } }

            return transformedLightData
        }
    }
}