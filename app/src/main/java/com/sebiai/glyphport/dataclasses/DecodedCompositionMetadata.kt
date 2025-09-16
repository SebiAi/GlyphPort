package com.sebiai.glyphport.dataclasses

import java.util.zip.Deflater
import java.util.zip.DeflaterInputStream
import kotlin.io.encoding.Base64

class DecodedCompositionMetadata(
    title: String,
    album: String,
    composer: String,
    author: String,
    custom1: String,
    custom2: String
) : CompositionMetadata(title, album, composer, author, custom1, custom2) {
    constructor() : this("", "",
        "", "", "", "")

    /**
     * Encodes the composition metadata
     */
    fun encode(): EncodedCompositionMetadata {
        val compressedAuthor = compress(author.encodeToByteArray())
        val compressedCustom1 = compress(custom1.encodeToByteArray())
        val b64 = Base64.withPadding(Base64.PaddingOption.ABSENT)
        val b64Autor = b64.encode(compressedAuthor)
        val b64Custom1 = b64.encode(compressedCustom1)

        return EncodedCompositionMetadata(
            title = title,
            album = album,
            composer = composer,
            author = b64Autor.chunked(76).joinToString("\n", postfix = "\n"),
            custom1 = b64Custom1.chunked(76).joinToString("\n", postfix = "\n"),
            custom2 = custom2
        )
    }

    private fun compress(bytes: ByteArray): ByteArray {
        return DeflaterInputStream(
            bytes.inputStream(),
            Deflater(Deflater.BEST_COMPRESSION)
        ).readBytes()
    }
}