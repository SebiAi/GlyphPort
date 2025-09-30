package com.sebiai.glyphport.dataclasses.transformer

import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.dataclasses.CompositionLightData
import com.sebiai.glyphport.dataclasses.LightDataTransformer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class DefaultPhone2aToPhone3aAndProLightDataTransformer(ioDispatcher: CoroutineDispatcher = Dispatchers.IO):
    LightDataTransformer(ioDispatcher) {
    final override val nameStringRes: Int
        get() = R.string.default_light_data_transformer_name
    final override val descriptionStringRes: Int
        get() = R.string.default_light_data_transformer_description
    override val handles: PhoneModel
        get() = PhoneModel.PHONE2A
    final override val outputs: PhoneModel
        get() = PhoneModel.PHONE3A_AND_PRO

    final override fun transformImpl(lightData: CompositionLightData): CompositionLightData {
        require(lightData.columns.toUInt() in handles.supportedZones)

        // Init final 1D array with final size
        val columns = outputs.supportedZones[0].toInt()
        val transformedLightData = ShortArray(columns * lightData.rows)

        lightData.forEachIndexed { i, row ->
            buildList {
                // Top left glyph of Phone (3a) / Phone (3a) Plus
                addAll(row.slice(0..3)) // Top left zones 0-3 move to 0-3
                add((row[4]/2 + row[5]/2).toShort()) // Average of top left zones 4-5 move to 4
                addAll(row.slice(6..9)) // Top left 4 zones move to 5-8
                add((row[10]/2 + row[11]/2).toShort()) // Average of top left zones 10-11 move to 9
                addAll(row.slice(12..15)) // Top left 4 zones move to 10-13
                add((row[16]/2 + row[17]/2).toShort()) // Average of top left zones 16-17 move to 14
                addAll(row.slice(18..21)) // Top left 4 zones move to 15-18
                add((row[22]/2 + row[23]/2).toShort()) // Average of top left zones 22-23 move to 19

                // Middle right glyph of Phone (3a) / Phone (3a) Plus
                repeat(11) { add(row[24]) } // Middle right moves to 20-30

                // Bottom right glyph of Phone (3a) / Phone (3a) Plus
                repeat(5) { add(row[25]) } // Bottom left moves to 31-25
            }.toShortArray()
                .copyInto(transformedLightData, i * columns)
        }
        return CompositionLightData(transformedLightData, columns)
    }
}