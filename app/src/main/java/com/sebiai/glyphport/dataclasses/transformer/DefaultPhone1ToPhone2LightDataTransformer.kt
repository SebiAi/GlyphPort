package com.sebiai.glyphport.dataclasses.transformer

import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.dataclasses.CompositionLightData
import com.sebiai.glyphport.dataclasses.LightDataTransformer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultPhone1ToPhone2LightDataTransformer(ioDispatcher: CoroutineDispatcher = Dispatchers.IO): LightDataTransformer(ioDispatcher) {
    override val nameStringRes: Int
        get() = R.string.default_light_data_transformer_name
    override val descriptionStringRes: Int
        get() = R.string.default_light_data_transformer_description

    override val handles: PhoneModel
        get() = PhoneModel.PHONE1
    override val outputs: PhoneModel
        get() = PhoneModel.PHONE2

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
                repeat (2) { add(row[0]) } // Camera - index 0-1, Camera top/bottom
                add(row[1]) // Diagonal - moves to 2
                repeat(16) { add(row[4]) } // Battery top right moves to 3-18, the 16 Zones in the Battery top right
                repeat(2) { add(row[5]) } // Battery top left moves to 19-20, the Battery top left and top vertical (left side)
                add(row[2]) // Battery bottom left moves to 21
                repeat(2) { add(row[3]) } // Battery bottom right moves to 22-23, the Battery bottom right and bottom vertical (right side)
                add(row[6]) // USB Dot moves to 24
                addAll(row.slice(7..14)) // USB Line (7-14) moves to 25-32
            }.toShortArray()
                .copyInto(transformedLightData, i * columns)
        }
        return CompositionLightData(transformedLightData, columns)
    }
}