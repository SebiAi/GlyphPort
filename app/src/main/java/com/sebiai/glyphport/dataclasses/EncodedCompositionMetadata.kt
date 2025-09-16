package com.sebiai.glyphport.dataclasses

import java.util.zip.DataFormatException
import java.util.zip.InflaterInputStream
import kotlin.io.encoding.Base64

class EncodedCompositionMetadata(
    title: String,
    album: String,
    composer: String,
    author: String,
    custom1: String,
    custom2: String
) : CompositionMetadata(title, album, composer, author, custom1, custom2) {
    constructor() : this("", "",
        "", "", "", "")

    class DecodeException(message: String, cause: Throwable): Exception(message, cause)

    /**
     * Decodes the composition metadata
     * @throws DecodeException When base64 decoding or zlib decompression fails
     */
    fun decode(): DecodedCompositionMetadata {
        val cleanedAuthor = author.replace("\n", "").trimEnd('=')
        val cleanedCustom1 = custom1.replace("\n", "").trimEnd('=')

        val b64 = Base64.withPadding(Base64.PaddingOption.ABSENT)
        val compressedAuthor: ByteArray
        val compressedCustom1: ByteArray
        try {
            compressedAuthor = b64.decode(cleanedAuthor)
            compressedCustom1 = b64.decode(cleanedCustom1)
        } catch (e: IllegalArgumentException) {
            throw DecodeException("Failed to base64 decode metadata", e)
        }

        val newAuthor: String
        val newCustom1: String
        try {
            newAuthor = decompress(compressedAuthor)
            newCustom1 = decompress(compressedCustom1)
        } catch (e: DataFormatException) {
            throw DecodeException("Failed to decompress metadata", e)
        }

        return DecodedCompositionMetadata(
            title = title,
            album = album,
            composer = composer,
            author = newAuthor,
            custom1 = newCustom1,
            custom2 = custom2
        )
    }

    private fun decompress(bytes: ByteArray): String {
        return InflaterInputStream(bytes.inputStream())
            .bufferedReader().use { it.readText() }
    }
}