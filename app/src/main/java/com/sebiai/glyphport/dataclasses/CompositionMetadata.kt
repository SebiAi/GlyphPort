package com.sebiai.glyphport.dataclasses

abstract class CompositionMetadata(
    val title: String,
    val album: String,
    val composer: String,
    val author: String,
    val custom1: String,
    val custom2: String
) {
    fun anyFieldEmpty(): Boolean {
        return anyFieldExceptCustom2Empty() || custom2.isEmpty()
    }

    fun anyFieldExceptCustom2Empty(): Boolean {
        return title.isEmpty() || album.isEmpty() ||
                composer.isEmpty() || author.isEmpty() ||
                custom1.isEmpty()
    }
}