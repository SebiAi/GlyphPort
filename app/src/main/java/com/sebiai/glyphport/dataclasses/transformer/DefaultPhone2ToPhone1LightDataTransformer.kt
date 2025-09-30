package com.sebiai.glyphport.dataclasses.transformer

import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.dataclasses.CompositionLightData
import com.sebiai.glyphport.dataclasses.LightDataTransformer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultPhone2ToPhone1LightDataTransformer(ioDispatcher: CoroutineDispatcher = Dispatchers.IO):
    LightDataTransformer(ioDispatcher) {
    override val nameStringRes: Int
        get() = R.string.default_light_data_transformer_name
    override val descriptionStringRes: Int
        get() = R.string.default_light_data_transformer_description
    override val handles: PhoneModel
        get() = PhoneModel.PHONE2
    override val outputs: PhoneModel
        get() = PhoneModel.PHONE1

    override fun transformImpl(lightData: CompositionLightData): CompositionLightData {
        require(lightData.columns.toUInt() in handles.supportedZones)

        // Init final 1D array with final size
        val columns = outputs.supportedZones[1].toInt() // This is a mapping to 15Cols!
        val transformedLightData = ShortArray(columns * lightData.rows)

        lightData.forEachIndexed { i, row ->
            buildList {
                // Camera glyph of Phone (1)
                add((row[0]/2 + row[1]/2).toShort()) // Average of Camera top and Camera bottom moves to 0

                // Diagonal glyph of Phone (1)
                add(row[2]) // Diagonal moves to 1

                // Battery bottom left zone of Phone (1)
                add(row[21]) // Battery bottom left moves to 2

                // Battery bottom right zone of Phone (1)
                add((row[22]/2 + row[23]/2).toShort()) // Average of Battery bottom right and Battery bottom vertical (right side) moves to 3

                // Battery top right zone of Phone (1)
                add((row.slice(3..18).sum()/16).toShort()) // Average of zones of Battery top right moves to 4

                // Battery top left zone of Phone (1)
                add((row[19]/2 + row[20]/2).toShort()) // Average of Battery top left and Battery top vertical (left side) moves to 5

                // USB Dot
                add(row[24]) // USB Dot moves to 6

                // USB Line
                addAll(row.slice(25..32)) // USB Line moves to 7-14
            }.toShortArray()
                .copyInto(transformedLightData, i * columns)
        }
        return CompositionLightData(transformedLightData, columns)
    }
}