package com.sebiai.glyphport.dataclasses.transformer

import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.dataclasses.CompositionLightData
import com.sebiai.glyphport.dataclasses.LightDataTransformer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class DefaultPhone1ToPhone2aLightDataTransformer(ioDispatcher: CoroutineDispatcher = Dispatchers.IO):
    LightDataTransformer(ioDispatcher) {
    final override val nameStringRes: Int
        get() = R.string.default_light_data_transformer_name
    final override val descriptionStringRes: Int
        get() = R.string.default_light_data_transformer_description
    final override val handles: PhoneModel
        get() = PhoneModel.PHONE1
    override val outputs: PhoneModel
        get() = PhoneModel.PHONE2A

    final override fun transformImpl(lightData: CompositionLightData): CompositionLightData {
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
                    // Top left glyph of Phone (2a)
                    addAll(row.slice(7..14)) // USB Line (7-14) moves to 0-7, the Top left bottom 8
                    repeat(4) { add(row[3]) } // Battery bottom right moves to 8-11
                    repeat(3) { add(row[2]) } // Battery bottom left moves to 12-14
                    repeat(3) { add(row[5]) } // Battery top left moves to 15-17
                    repeat(4) { add(row[4]) } // Battery top right moves to 18-21
                    repeat(2) { add(row[0]) } // Camera moves to 22-23

                    // Middle right glyph of Phone (2a)
                    add(row[1]) // Diagonal to Middle right

                    // Bottom left glyph of Phone (2a)
                    add(row[6])
                }
            }
        )
    }
}