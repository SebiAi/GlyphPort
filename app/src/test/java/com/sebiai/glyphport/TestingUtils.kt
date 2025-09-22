package com.sebiai.glyphport

import com.sebiai.glyphport.dataclasses.CompositionLightData

object CompositionLightDataLoader {
    fun fromResource(path: String): CompositionLightData {
        val content = this.javaClass.classLoader!!.getResourceAsStream(path).use {
            String(it.readAllBytes())
        }

        // This is a copy of the code in "CompositionValidator" but whatever
        val lightData = content.replace("\r\n", "\n").split('\n')
            .map { it.trim().removeSuffix(",") }
            .filter { it.isNotEmpty() }
            .map { it.split(',') }
            .map { it.map { brightness -> brightness.toUInt() } }

        return CompositionLightData(lightData)
    }
}

