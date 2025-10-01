package com.sebiai.glyphport.dataclasses.transformer

import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.dataclasses.CompositionLightData
import com.sebiai.glyphport.dataclasses.LightDataTransformer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class DefaultPhone2aToPhone3LightDataTransformer(ioDispatcher: CoroutineDispatcher = Dispatchers.IO):
    LightDataTransformer(ioDispatcher) {
    final override val nameStringRes: Int
        get() = R.string.default_light_data_transformer_name
    final override val descriptionStringRes: Int
        get() = R.string.default_light_data_transformer_description
    override val handles: PhoneModel
        get() = PhoneModel.PHONE2A
    final override val outputs: PhoneModel
        get() = PhoneModel.PHONE3

    final override fun transformImpl(lightData: CompositionLightData): CompositionLightData {
        require(lightData.columns.toUInt() in handles.supportedZones)

        // Init final 1D array with final size
        val columns = outputs.supportedZones[0].toInt()
        val transformedLightData = ShortArray(columns * lightData.rows)

        lightData.forEachIndexed { i, row ->
            buildList {
                // First three rows
                repeat(75) { add(0) }
                // 4th row
                repeat(6) { add(0) }
                repeat(2) { add((row.slice(18..20).sum()/3).toShort()) } // Three zones of top left (2)
                add((row.slice(21..23).sum()/3).toShort()) // Most right three zones of top left
                repeat(16) { add(0) }
                // 5th row
                repeat(5) { add(0) }
                repeat(2) { add((row.slice(15..17).sum()/3).toShort()) } // Three zones of top left (3)
                add((row.slice(18..20).sum()/3).toShort()) // Three zones of top left (2)
                repeat(17) { add(0) }
                // 6th row
                repeat(4) { add(0) }
                repeat(2) { add((row.slice(12..14).sum()/3).toShort()) } // Three zones of top left (4)
                add((row.slice(15..17).sum()/3).toShort()) // Three zones of top left (3)
                repeat(18) { add(0) }
                // 7th row
                repeat(3) { add(0) }
                repeat(2) { add((row.slice(9..11).sum()/3).toShort()) } // Three zones of top left (5)
                add((row.slice(12..14).sum()/3).toShort()) // Three zones of top left (4)
                repeat(19) { add(0) }
                // 8th row
                repeat(3) { add(0) }
                add((row.slice(6..8).sum()/3).toShort()) // Three zones of top left (6)
                add((row.slice(9..11).sum()/3).toShort()) // Three zones of top left (5)
                repeat(16) { add(0) }
                add(row[24]) // Middle right
                repeat(3) { add(0) }
                // 9th row
                repeat(2) { add(0) }
                add((row.slice(3..5).sum()/3).toShort()) // Three zones of top left (7)
                repeat(2) { add((row.slice(6..8).sum()/3).toShort()) } // Three zones of top left (6)
                repeat(15) { add(0) }
                repeat(3) { add(row[24]) } // Middle right
                repeat(2) { add(0) }
                // 10th row
                repeat(2) { add(0) }
                repeat(2) { add((row.slice(3..5).sum()/3).toShort()) } // Three zones of top left (7)
                repeat(16) { add(0) }
                repeat(3) { add(row[24]) } // Middle right
                repeat(2) { add(0) }
                // 11th row
                repeat(2) { add(0) }
                add((row.slice(0..2).sum()/3).toShort()) // Most left three zones of top left (8)
                repeat(17) { add(0) }
                repeat(3) { add(row[24]) } // Middle right
                repeat(2) { add(0) }
                // 12th row
                repeat(20) { add(0) }
                repeat(3) { add(row[24]) } // Middle right
                repeat(2) { add(0) }
                // 13th row
                repeat(20) { add(0) }
                repeat(3) { add(row[24]) } // Middle right
                repeat(2) { add(0) }
                // 14th row
                repeat(20) { add(0) }
                repeat(3) { add(row[24]) } // Middle right
                repeat(2) { add(0) }
                // 15th row
                repeat(20) { add(0) }
                repeat(3) { add(row[24]) } // Middle right
                repeat(2) { add(0) }
                // 16th row
                repeat(20) { add(0) }
                repeat(3) { add(row[24]) } // Middle right
                repeat(2) { add(0) }
                // 17th row
                repeat(3) { add(0) }
                add(row[25]) // Bottom left
                repeat(16) { add(0) }
                repeat(3) { add(row[24]) } // Middle right
                repeat(2) { add(0) }
                // 18th row
                repeat(3) { add(0) }
                repeat(2) { add(row[25]) } // Bottom left
                repeat(16) { add(0) }
                add(row[24]) // Middle right
                repeat(3) { add(0) }
                // 19th row
                repeat(3) { add(0) }
                repeat(3) { add(row[25]) } // Bottom left
                repeat(19) { add(0) }
                // 20th row
                repeat(4) { add(0) }
                repeat(3) { add(row[25]) } // Bottom left
                repeat(18) { add(0) }
                // 21st row
                repeat(5) { add(0) }
                repeat(2) { add(row[25]) } // Bottom left
                repeat(18) { add(0) }
                // 22nd row and onwards
                repeat(25 * 4) { add(0) }
            }.toShortArray()
                .copyInto(transformedLightData, i * columns)
        }
        return CompositionLightData(transformedLightData, columns)
    }
}