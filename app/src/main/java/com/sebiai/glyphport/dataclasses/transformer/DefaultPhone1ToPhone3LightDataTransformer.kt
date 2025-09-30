package com.sebiai.glyphport.dataclasses.transformer

import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.dataclasses.CompositionLightData
import com.sebiai.glyphport.dataclasses.LightDataTransformer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultPhone1ToPhone3LightDataTransformer(ioDispatcher: CoroutineDispatcher = Dispatchers.IO):
    LightDataTransformer(ioDispatcher) {
    override val nameStringRes: Int
        get() = R.string.default_light_data_transformer_name
    override val descriptionStringRes: Int
        get() = R.string.default_light_data_transformer_description
    override val handles: PhoneModel
        get() = PhoneModel.PHONE1
    override val outputs: PhoneModel
        get() = PhoneModel.PHONE3

    override fun transformImpl(lightData: CompositionLightData): CompositionLightData {
        // Sanity check
        assert(5u in PhoneModel.PHONE1.supportedZones && 15u in PhoneModel.PHONE1.supportedZones && PhoneModel.PHONE1.supportedZones.size == 2)

        require(lightData.columns.toUInt() in handles.supportedZones)
        if (lightData.columns == 5) {
            return transform15Cols(TransformerUtils.phone1Transform5To15Cols(lightData))
        }
        // 15Cols
        return transform15Cols(lightData)
    }

    private fun transform15Cols(lightData: CompositionLightData): CompositionLightData {
        // Init final 1D array with final size
        val columns = outputs.supportedZones[0].toInt()
        val transformedLightData = ShortArray(columns * lightData.rows)

        lightData.forEachIndexed { i, row ->
            buildList {
                // First two rows empty
                repeat(50) { add(0) }
                // 3rd row
                repeat(9) { add(0) }
                repeat(2) { add(row[0]) } // Camera
                repeat(5) { add(0) }
                add(row[1]) // Diagonal
                repeat(8) { add(0) }
                // 4th row
                repeat(8) { add(0) }
                add(row[0])  // Camera
                repeat(6) { add(0) }
                add(row[1])  // Diagonal
                repeat(9) { add(0) }
                // 5th row
                repeat(8) { add(0) }
                add(row[0])  // Camera
                repeat(2) { add(0) }
                add(row[0])
                repeat(2) { add(0) }
                add(row[1])  // Diagonal
                repeat(10) { add(0) }
                // 6th row
                repeat(9) { add(0) }
                repeat(2) { add(row[0]) } // Camera
                repeat(14) { add(0) }
                // 7th row
                repeat(25) { add(0) }
                // 8th row
                repeat(11) { add(0) }
                repeat(3) { add(row[4]) } // Battery top right
                repeat(11) { add(0) }
                // 9th row
                repeat(9) { add(0) }
                add(row[5]) // Battery top left
                add(row[4]) // Battery top right
                repeat(3) { add(0) }
                repeat(2) { add(row[4]) } // Battery top right
                repeat(9) { add(0) }
                // 10th row
                repeat(8) { add(0) }
                add(row[5]) // Battery top left
                repeat(7) { add(0) }
                add(row[4]) // Battery top right
                repeat(8) { add(0) }
                // 11th row
                repeat(8) { add(0) }
                add(row[5]) // Battery top left
                repeat(16) { add(0) }
                // 12th row
                repeat(8) { add(0) }
                add(row[5]) // Battery top left
                repeat(16) { add(0) }
                // 13th row
                repeat(8) { add(0) }
                add(row[5]) // Battery top left
                repeat(7) { add(0) }
                add(row[3]) // Battery bottom right
                repeat(8) { add(0) }
                // 14th row
                repeat(8) { add(0) }
                add(row[5]) // Battery top left
                repeat(7) { add(0) }
                add(row[3]) // Battery bottom right
                repeat(8) { add(0) }
                // 15th row
                repeat(8) { add(0) }
                add(row[2]) // Battery bottom left
                repeat(7) { add(0) }
                add(row[3]) // Battery bottom right
                repeat(8) { add(0) }
                // 16th row
                repeat(9) { add(0) }
                repeat(2) { add(row[2]) } // Battery bottom left
                repeat(3) { add(0) }
                repeat(2) { add(row[3]) } // Battery bottom right
                repeat(9) { add(0) }
                // 17th row
                repeat(11) { add(0) }
                repeat(3) { add(row[2]) } // Battery bottom left
                repeat(11) { add(0) }
                // 18th row
                repeat(25) { add(0) }
                // 19th row
                repeat(12) { add(0) }
                add((row[14]/2 + row[13]/2).toShort()) // Average two zones of USB Line together
                repeat(12) { add(0) }
                // 20th row
                repeat(12) { add(0) }
                add((row[12]/2 + row[11]/2).toShort()) // Average two zones of USB Line together
                repeat(12) { add(0) }
                // 21th row
                repeat(12) { add(0) }
                add((row[10]/2 + row[9]/2).toShort()) // Average two zones of USB Line together
                repeat(12) { add(0) }
                // 22th row
                repeat(12) { add(0) }
                add((row[8]/2 + row[7]/2).toShort()) // Average two zones of USB Line together
                repeat(12) { add(0) }
                // 23th row
                repeat(25) { add(0) }
                // 24th row
                repeat(12) { add(0) }
                add(row[6]) // USB Dot
                repeat(12) { add(0) }
                // 25th row
                repeat(25) { add(0) }
            }.toShortArray()
                .copyInto(transformedLightData, i * columns)
        }
        return CompositionLightData(transformedLightData, columns)
    }
}