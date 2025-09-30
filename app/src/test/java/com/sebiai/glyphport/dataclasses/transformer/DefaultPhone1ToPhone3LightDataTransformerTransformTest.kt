package com.sebiai.glyphport.dataclasses.transformer

import com.sebiai.glyphport.CompositionLightDataLoader
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

// See here on more info on how to test with coroutines: https://developer.android.com/kotlin/coroutines/test

class DefaultPhone1ToPhone3LightDataTransformerTransformTest {
    private val testDispatcher = StandardTestDispatcher()

    // Can share the same transformer object between tests since the class only has non writable
    // internal state
    private val transformer = DefaultPhone1ToPhone3LightDataTransformer(testDispatcher)

    @Test
    fun given15ColsPhone1CompositionLightData_callTransform_expectValidPhone3CompositionLightData() = runTest(testDispatcher.scheduler) {
        val lightData = CompositionLightDataLoader.fromResource("transformer/DefaultPhone1ToPhone3/15ColsPhone1Input.csv")
        val expectedLightData = CompositionLightDataLoader.fromResource("transformer/DefaultPhone1ToPhone3/15ColsExpectedPhone3Output.csv")

        val result = transformer.transform(lightData)

        assertEquals(expectedLightData, result)
    }
}