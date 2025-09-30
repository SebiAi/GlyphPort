package com.sebiai.glyphport.dataclasses.transformer

import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.dataclasses.CompositionLightData
import com.sebiai.glyphport.dataclasses.LightDataTransformer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultPhone1ToPhone3aAndProLightDataTransformer(ioDispatcher: CoroutineDispatcher = Dispatchers.IO):
    LightDataTransformer(ioDispatcher) {
    override val nameStringRes: Int
        get() = R.string.default_light_data_transformer_name
    override val descriptionStringRes: Int
        get() = R.string.default_light_data_transformer_description
    override val handles: PhoneModel
        get() = PhoneModel.PHONE1
    override val outputs: PhoneModel
        get() = PhoneModel.PHONE3A_AND_PRO

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
                    // Top left glyph of Phone (3a)
                    repeat(5) { add(row[3]) } // Battery bottom right moves to 0-4
                    repeat(5) { add(row[2]) } // Battery bottom left moves to 5-9
                    repeat(5) { add(row[5]) } // Battery top left moves to 10-14
                    repeat(5) { add(row[4]) } // Battery top right moves to 15-19

                    // Middle right glyph of Phone (3a)
                    repeat(3) { add(row[1]) } // Diagonal moves to 20-22
                    addAll(row.subList(7, 15).asReversed()) // USB Line moves to 23-30

                    // Bottom left glyph of Phone (3a)
                    repeat(2) { add(row[6]) } // USB Dot moves to 31-32
                    repeat(3) { add(row[0]) } // Camera moves to 33-35
                }
            }
        )
    }
}