package com.sebiai.glyphport.dataclasses

import android.content.Context
import android.net.Uri
import android.util.Log
import com.sebiai.glyphport.useTempFile
import com.sebiai.glyphport.utils.OpusMetadataUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CompositionMetadataWriter(
    val audioFile: Uri,
    val outputFile: Uri,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun write(context: Context, metadata: EncodedCompositionMetadata): Boolean = withContext(ioDispatcher) {
        return@withContext audioFile.useTempFile(context) { tempFile ->
            // Prepare metadata arrays
            val metaMap = mapOf(
                "TITLE" to metadata.title,
                "ALBUM" to metadata.album,
                "AUTHOR" to metadata.author,
                "COMPOSER" to metadata.composer,
                "CUSTOM1" to metadata.custom1,
                "CUSTOM2" to metadata.custom2
            )
            
            val keys = metaMap.keys.toTypedArray()
            val values = metaMap.values.toTypedArray()

            // Write metadata using native TagLib
            val success = OpusMetadataUtil.writeOpusMetadata(tempFile.absolutePath, keys, values)
            
            if (!success) {
                Log.e("CompositionMetadataWriter", "Failed to write metadata to temp file")
                return@useTempFile false
            }

            // Write temp file to output Uri
            try {
                context.contentResolver.openOutputStream(outputFile)?.use { output ->
                     tempFile.inputStream().use { input ->
                         input.copyTo(output)
                     }
                } ?: return@useTempFile false
            } catch (e: Exception) {
                 Log.e("CompositionMetadataWriter", "Failed to copy temp file to output", e)
                 return@useTempFile false
            }

            return@useTempFile true
        } ?: run {
             Log.e("CompositionMetadataWriter", "Failed to copy audio URI to temp file")
             false
        }
    }
}
