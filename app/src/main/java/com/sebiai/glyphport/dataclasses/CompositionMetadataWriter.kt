package com.sebiai.glyphport.dataclasses

import android.content.Context
import android.net.Uri
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.sebiai.glyphport.safeHandleFFmpegKitSession
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File

class CompositionMetadataWriter(
    val audioFile: Uri,
    val outputFile: Uri,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun write(context: Context, metadata: EncodedCompositionMetadata): Boolean = withContext(ioDispatcher) {
        // Convert the metadata to ffmetadata (https://ffmpeg.org/ffmpeg-formats.html#ffmetadata)
        // This data can then be piped to ffmpeg circumventing errors when passing
        // long metadata via arguments due to command length limitations.
        val metadata = arrayOf(
            "TITLE" to metadata.title,
            "ALBUM" to metadata.album,
            "AUTHOR" to metadata.author,
            "COMPOSER" to metadata.composer,
            "CUSTOM1" to metadata.custom1,
            "CUSTOM2" to metadata.custom2
        )
        val ffmetadataContent = metadata.joinToString(
            separator = "\n",
            prefix = ";FFMETADATA1\n",
            postfix = "\n"
        ) { (key, value) ->
            "${escapeFFmetadata(key)}=${escapeFFmetadata(value)}"
        }

        val deferredFFmpegSuccess: Deferred<Boolean>
        val pipe = FFmpegKitConfig.registerNewFFmpegPipe(context)
        try {
            // Construct command with pipe
            val audioFileParam = FFmpegKitConfig.getSafParameterForRead(context, audioFile)
            val outputFileParam = FFmpegKitConfig.getSafParameterForWrite(context, outputFile)
            val ffmpegCommand = metadata.joinToString(
                separator = " ",
                prefix = "-i '$audioFileParam' -i $pipe ", // Inputs
                postfix = " -map_metadata 1 -c:a copy -fflags +bitexact -flags:v +bitexact " +
                        "-flags:a +bitexact '$outputFileParam'" // Copy metadata without pollution
            ) { (key, _) ->
                "-metadata:s:a:0 '$key='" // Clear all metadata we want to overwrite
            }

            // Execute async - we need to write to the pipe while ffmpeg is running
            deferredFFmpegSuccess = async {
                val session = FFmpegKit.execute(ffmpegCommand)
                var success = false
                safeHandleFFmpegKitSession(
                    session,
                    onSuccess = { success = true },
                    onCancel = {  },
                    onFailure = { }
                )
                return@async success
            }

            // Write ffmetadata to pipe
            File(pipe).writeText(ffmetadataContent)
        } finally {
            FFmpegKitConfig.closeFFmpegPipe(pipe)
        }

        return@withContext deferredFFmpegSuccess.await()
    }

    private fun escapeFFmetadata(content: String): String {
        // Special characters need to be escaped with a backslash ('\', '=', ';', '#', '\n')
        return content
            .replace("""\""", """\\""")
            .replace("""=""", """\=""")
            .replace(""";""", """\;""")
            .replace("""#""", """\#""")
            .replace("\n", "\\\n")
    }
}