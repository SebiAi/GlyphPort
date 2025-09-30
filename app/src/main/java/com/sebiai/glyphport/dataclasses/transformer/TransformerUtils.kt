package com.sebiai.glyphport.dataclasses.transformer

import com.sebiai.glyphport.dataclasses.CompositionLightData

class TransformerUtils {
    companion object {
        fun phone1Transform5To15Cols(lightData: CompositionLightData): CompositionLightData {
            return CompositionLightData(
                lightData.map { row ->
                    buildList {
                        add(row[0]) // Camera - stays at index 0
                        add(row[1]) // Diagonal - stays at index 1
                        repeat(4) { add(row[2]) } // Battery - repeated 4 times (index 2-5, the 4 zones in Battery)
                        add(row[4]) // USB Dot - is moved from index 4 to index 6
                        repeat(8) { add(row[3]) } // USB Line - is moved from index 5 to indexes 7-14, the 8 Zones in USB Line
                    }
                }.toList()
            )
        }
    }
}