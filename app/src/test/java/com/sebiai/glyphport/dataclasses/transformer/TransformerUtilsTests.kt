package com.sebiai.glyphport.dataclasses.transformer

import com.sebiai.glyphport.CompositionLightDataLoader
import org.junit.Assert.assertEquals
import org.junit.Test

class TransformerUtilsTests {
    @Test
    fun given5ColsPhone1CompositionLightData_callPhone1Transform5To15Cols_expectValid15ColsPhone1CompositionLightData() {
        val lightData = CompositionLightDataLoader.fromResource("transformer/TransformerUtils/5ColsPhone1Input.csv")
        val expectedLightData = CompositionLightDataLoader.fromResource("transformer/TransformerUtils/Expected15ColsPhone1Output.csv")

        val result = TransformerUtils.phone1Transform5To15Cols(lightData)

        assertEquals(expectedLightData, result)
    }
}