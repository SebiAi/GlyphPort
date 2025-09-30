package com.sebiai.glyphport.dataclasses.transformer

import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.dataclasses.CompositionLightData
import com.sebiai.glyphport.dataclasses.LightDataTransformer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultPhone2ToPhone3LightDataTransformer(ioDispatcher: CoroutineDispatcher = Dispatchers.IO):
    LightDataTransformer(ioDispatcher) {
    override val nameStringRes: Int
        get() = R.string.default_light_data_transformer_name
    override val descriptionStringRes: Int
        get() = R.string.default_light_data_transformer_description
    override val handles: PhoneModel
        get() = PhoneModel.PHONE2
    override val outputs: PhoneModel
        get() = PhoneModel.PHONE3

    override fun transformImpl(lightData: CompositionLightData): CompositionLightData {
        require(lightData.columns.toUInt() in handles.supportedZones)

        // Init final 1D array with final size
        val columns = outputs.supportedZones[0].toInt()
        val transformedLightData = ShortArray(columns * lightData.rows)

        lightData.forEachIndexed { i, row ->
            buildList {
                // First two rows empty
                repeat(50) { add(0) }
                // 3rd row
                repeat(9) { add(0) }
                repeat(2) { add(row[0]) } // Camera top
                repeat(5) { add(0) }
                add(row[2]) // Diagonal
                repeat(8) { add(0) }
                // 4th row
                repeat(8) { add(0) }
                add(row[0])  // Camera top
                repeat(6) { add(0) }
                add(row[2])  // Diagonal
                repeat(9) { add(0) }
                // 5th row
                repeat(11) { add(0) }
                add(row[1]) // Camera bottom
                repeat(2) { add(0) }
                add(row[2])  // Diagonal
                repeat(10) { add(0) }
                // 6th row
                repeat(9) { add(0) }
                repeat(2) { add(row[1]) } // Camera bottom
                repeat(14) { add(0) }
                // 7th row
                repeat(25) { add(0) }
                // 8th row
                repeat(11) { add(0) }
                add((row.slice(15..18).sum()/4).toShort()) // Average of 4 most left zones of Battery top right
                add((row.slice(12..14).sum()/3).toShort()) // Average of 3 zones of Battery top right
                add((row.slice(9..11).sum()/3).toShort()) // Average of 3 zones of Battery top right
                add((row.slice(6..8).sum()/3).toShort()) // Average of 3 zones of Battery top right
                repeat(10) { add(0) }
                // 9th row
                repeat(9) { add(0) }
                add(row[19]) // Battery top left
                repeat(5) { add(0) }
                add((row.slice(3..5).sum()/3).toShort())  // Average of 3 most right zones of Battery top right
                repeat(9) { add(0) }
                // 10th row
                repeat(8) { add(0) }
                add(row[19]) // Battery top left
                repeat(16) { add(0) }
                // 11th row
                repeat(25) { add(0) }
                // 12th row
                repeat(8) { add(0) }
                add(row[20]) // Battery top vertical (left side)
                repeat(7) { add(0) }
                add(row[23]) // Battery bottom vertical (right side)
                repeat(8) { add(0) }
                // 13th row
                repeat(8) { add(0) }
                add(row[20]) // Battery top vertical (left side)
                repeat(7) { add(0) }
                add(row[23]) // Battery bottom vertical (right side)
                repeat(8) { add(0) }
                // 14th row
                repeat(25) { add(0) }
                // 15th row
                repeat(16) { add(0) }
                add(row[22]) // Battery bottom right
                repeat(8) { add(0) }
                // 16th row
                repeat(9) { add(0) }
                repeat(2) { add(row[21]) } // Battery bottom left
                repeat(4) { add(0) }
                add(row[22]) // Battery bottom right
                repeat(9) { add(0) }
                // 17th row
                repeat(11) { add(0) }
                repeat(3) { add(row[21]) } // Battery bottom left
                repeat(11) { add(0) }
                // 18th row
                repeat(25) { add(0) }
                // 19th row
                repeat(12) { add(0) }
                add((row[31]/2 + row[32]/2).toShort()) // Average two zones of USB Line together
                repeat(12) { add(0) }
                // 20th row
                repeat(12) { add(0) }
                add((row[29]/2 + row[30]/2).toShort()) // Average two zones of USB Line together
                repeat(12) { add(0) }
                // 21th row
                repeat(12) { add(0) }
                add((row[27]/2 + row[28]/2).toShort()) // Average two zones of USB Line together
                repeat(12) { add(0) }
                // 22th row
                repeat(12) { add(0) }
                add((row[25]/2 + row[26]/2).toShort()) // Average two zones of USB Line together
                repeat(12) { add(0) }
                // 23th row
                repeat(25) { add(0) }
                // 24th row
                repeat(12) { add(0) }
                add(row[24]) // USB Dot
                repeat(12) { add(0) }
                // 25th row
                repeat(25) { add(0) }
            }.toShortArray()
                .copyInto(transformedLightData, i * columns)
        }
        return CompositionLightData(transformedLightData, columns)
    }
}