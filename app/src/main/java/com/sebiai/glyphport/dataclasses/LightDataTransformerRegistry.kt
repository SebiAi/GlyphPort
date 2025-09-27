package com.sebiai.glyphport.dataclasses

import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.dataclasses.transformer.DefaultPhone1ToPhone2LightDataTransformer
import kotlinx.coroutines.Dispatchers

// Interface would be required here if we want to make it testable.
object LightDataTransformerRegistry {
    /**
     * First element in the pair is the input phone model, second one the output phone model of the
     * transformer group.
     */
    private val collections by lazy { // Thread safe initialization
        createTransformerCollections().associateBy { it.handles to it.outputs }
    }

    fun getSupportedConversions(input: PhoneModel): Set<PhoneModel> {
        return collections.keys
            .filter { it.first == input }
            .map { it.second }
            .toSet()
    }

    fun getCollection(input: PhoneModel, output: PhoneModel): LightDataTransformerCollection? {
        return collections[input to output]
    }

    private fun createTransformerCollections(): List<LightDataTransformerCollection> {
        return listOf(
            LightDataTransformerCollection(
                handles = PhoneModel.PHONE1,
                outputs = PhoneModel.PHONE2,
                transformers = listOf(
                    DefaultPhone1ToPhone2LightDataTransformer(),
                    object : LightDataTransformer(Dispatchers.IO) {
                        override val nameStringRes: Int
                            get() = R.string.testing_light_data_transformer_name
                        override val descriptionStringRes: Int
                            get() = R.string.testing_light_data_transformer_description
                        override val handles: PhoneModel
                            get() = PhoneModel.PHONE1
                        override val outputs: PhoneModel
                            get() = PhoneModel.PHONE2

                        override fun transformImpl(lightData: CompositionLightData): CompositionLightData {
                            return lightData
                        }

                    }
                )
            )
            // TODO: MORE TRANSFORMERS
        )
    }
}