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
        assert(5u in PhoneModel.PHONE1.supportedZones && 15u in PhoneModel.PHONE1.supportedZones && PhoneModel.PHONE1.supportedZones .size == 2)

        require(lightData.columns.toUInt() in PhoneModel.PHONE1.supportedZones)
        if (lightData.columns == 5) {
            return transform15Cols(TransformerUtils.phone1Transform5To15Cols(lightData))
        }
        // 15Cols
        return transform15Cols(lightData)
    }

    private fun transform15Cols(lightData: CompositionLightData): CompositionLightData {
        return CompositionLightData(
            lightData.map { row ->
                buildList {
                    // First two rows empty
                    repeat(50) { add(0u) }
                    // 3rd row
                    repeat(9) { add(0u) }
                    repeat(2) { add(row[0]) } // Camera
                    repeat(5) { add(0u) }
                    repeat(1) { add(row[1]) } // Diagonal
                    repeat(8) { add(0u) }
                    // 4th row
                    repeat(8) { add(0u) }
                    repeat(1) { add(row[0]) } // Camera
                    repeat(6) { add(0u) }
                    repeat(1) { add(row[1]) } // Diagonal
                    repeat(9) { add(0u) }
                    // 5th row
                    repeat(8) { add(0u) }
                    repeat(1) { add(row[0]) } // Camera
                    repeat(5) { add(0u) }
                    repeat(1) { add(row[1]) } // Diagonal
                    repeat(10) { add(0u) }
                    // 6th row
                    repeat(9) { add(0u) }
                    repeat(2) { add(row[0]) } // Camera
                    repeat(14) { add(0u) }
                    // 7th row
                    repeat(25) { add(0u) }
                    // 8th row
                    repeat(11) { add(0u) }
                    repeat(3) { add(row[4]) } // Battery top right
                    repeat(11) { add(0u) }
                    // 9th row
                    repeat(9) { add(0u) }
                    add(row[5]) // Battery top left
                    add(row[4]) // Battery top right
                    repeat(3) { add(0u) }
                    repeat(2) { add(row[4]) } // Battery top right
                    repeat(9) { add(0u) }
                    // 10th row
                    repeat(8) { add(0u) }
                    add(row[5]) // Battery top left
                    repeat(7) { add(0u) }
                    add(row[4]) // Battery top right
                    repeat(8) { add(0u) }
                    // 11th row
                    repeat(8) { add(0u) }
                    add(row[5]) // Battery top left
                    repeat(16) { add(0u) }
                    // 12th row
                    repeat(8) { add(0u) }
                    add(row[5]) // Battery top left
                    repeat(16) { add(0u) }
                    // 13th row
                    repeat(8) { add(0u) }
                    add(row[5]) // Battery top left
                    repeat(7) { add(0u) }
                    add(row[3]) // Battery bottom right
                    repeat(8) { add(0u) }
                    // 14th row
                    repeat(8) { add(0u) }
                    add(row[5]) // Battery top left
                    repeat(7) { add(0u) }
                    add(row[3]) // Battery bottom right
                    repeat(8) { add(0u) }
                    // 15th row
                    repeat(8) { add(0u) }
                    add(row[2]) // Battery bottom left
                    repeat(7) { add(0u) }
                    add(row[3]) // Battery bottom right
                    repeat(8) { add(0u) }
                    // 16th row
                    repeat(9) { add(0u) }
                    repeat(2) { add(row[2]) } // Battery bottom left
                    repeat(3) { add(0u) }
                    repeat(2) { add(row[3]) } // Battery bottom right
                    repeat(9) { add(0u) }
                    // 17th row
                    repeat(11) { add(0u) }
                    repeat(3) { add(row[2]) } // Battery bottom left
                    repeat(11) { add(0u) }
                    // 18th row
                    repeat(25) { add(0u) }
                    // 19th row
                    repeat(12) { add(0u) }
                    add((row[14]/2u + row[13]/2u).toUShort()) // Average two zones of USB Line together
                    repeat(12) { add(0u) }
                    // 20th row
                    repeat(12) { add(0u) }
                    add((row[12]/2u + row[11]/2u).toUShort()) // Average two zones of USB Line together
                    repeat(12) { add(0u) }
                    // 21th row
                    repeat(12) { add(0u) }
                    add((row[10]/2u + row[9]/2u).toUShort()) // Average two zones of USB Line together
                    repeat(12) { add(0u) }
                    // 22th row
                    repeat(12) { add(0u) }
                    add((row[8]/2u + row[7]/2u).toUShort()) // Average two zones of USB Line together
                    repeat(12) { add(0u) }
                    // 23th row
                    repeat(25) { add(0u) }
                    // 24th row
                    repeat(12) { add(0u) }
                    add(row[6]) // USB Dot
                    repeat(12) { add(0u) }
                    // 25th row
                    repeat(25) { add(0u) }
                }
            }
        )
    }
}