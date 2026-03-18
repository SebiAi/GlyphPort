package com.sebiai.glyphport.utils

import android.util.Log

object OpusMetadataUtil {
    init {
        try {
            System.loadLibrary("libopus-metadata-util")
        } catch (e: UnsatisfiedLinkError) {
            Log.e("OpusMetadataUtil", "Failed to load native library", e)
        }
    }

    /**
     * Writes custom metadata to an Opus/Ogg file.
     * @param path Absolute path to the file - must be writable standard [java.io.File] path.
     * @param keys Array of metadata keys (e.g. "TITLE", "CUSTOM1").
     * @param values Array of corresponding values.
     * @return true if successful, false otherwise.
     */
    external fun writeOpusMetadata(path: String, keys: Array<String>, values: Array<String>): Boolean

    /**
     * Reads metadata from an Opus/Ogg file.
     * @param path Absolute path to the file - must be a standard [java.io.File] path.
     * @return Map of metadata keys and values, or null if failed.
     */
    external fun readOpusMetadata(path: String): Map<String, String>?
}
