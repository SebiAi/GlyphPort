package com.sebiai.glyphport.dataclasses

import android.content.Context
import android.net.Uri
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.sebiai.glyphport.safeHandleFFmpegKitSession
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class CompositionMetadataWriter(
    val audioFile: Uri,
    val outputFile: Uri,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun write(context: Context, metadata: EncodedCompositionMetadata) = withContext(ioDispatcher) {
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

        val pipe = FFmpegKitConfig.registerNewFFmpegPipe(context)
        try {
            // Construct command with pipe
            val audioFileParam = FFmpegKitConfig.getSafParameterForRead(context, audioFile)
            val outputFileParam = FFmpegKitConfig.getSafParameterForWrite(context, outputFile)
            val ffmpegCommand = metadata.joinToString( // TODO: Test if files with spaces work!
                separator = " ",
                prefix = "-i '$audioFileParam' -i $pipe ", // Inputs
                postfix = " -map_metadata 1 -c:a copy -fflags +bitexact -flags:v +bitexact " +
                        "-flags:a +bitexact '$outputFileParam'" // Copy metadata without pollution
            ) { (key, _) ->
                "-metadata:s:a:0 '$key='" // Clear all metadata we want to overwrite
            }

            // Execute async
            FFmpegKit.executeAsync(ffmpegCommand) {
                safeHandleFFmpegKitSession(
                    session = it,
                    onSuccess = {}
                )
            }

            // Write ffmetadata to pipe
            File(pipe).writeText(ffmetadataContent)
        } finally {
            FFmpegKitConfig.closeFFmpegPipe(pipe)
        }
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