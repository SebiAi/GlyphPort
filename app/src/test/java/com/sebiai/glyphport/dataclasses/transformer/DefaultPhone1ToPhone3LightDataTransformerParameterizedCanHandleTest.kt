package com.sebiai.glyphport.dataclasses.transformer

import com.sebiai.glyphport.PhoneModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class DefaultPhone1ToPhone3LightDataTransformerParameterizedCanHandleTest(
    val invalidPhoneModelPair: Pair<PhoneModel, PhoneModel>,
    val expected: Boolean
) {
    companion object {
        private val validPhoneModelPair = PhoneModel.PHONE1 to PhoneModel.PHONE3

        // Type Any should be Pair<PhoneModel, PhoneModel> and then Boolean
        @JvmStatic
        @Parameterized.Parameters
        fun provideInvalidPhoneModelPairsForCallCanHandle(): List<Array<Any>> {
            val entries = PhoneModel.entries

            // Generate all combination of PhoneModels except for the valid one
            val tests = entries.map { first ->
                entries.map { second -> first to second }
            }.flatten()
                .map { arrayOf<Any>(it, it == validPhoneModelPair) }

            return tests
        }
    }

    // Can share the same transformer object between tests since the class only has non writable
    // internal state
    private val transformer = DefaultPhone1ToPhone3LightDataTransformer()

    @Test
    fun givenPhoneModelPair_callCanHandle_expectExpectedResult() {
        println("Parameters: $invalidPhoneModelPair")
        assertEquals(
            expected,
            transformer.canHandle(invalidPhoneModelPair.first, invalidPhoneModelPair.second)
        )
    }

}