package com.sebiai.glyphport.dataclasses

import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.dataclasses.transformer.DefaultPhone1ToPhone2LightDataTransformer
import com.sebiai.glyphport.dataclasses.transformer.DefaultPhone1ToPhone2aLightDataTransformer
import com.sebiai.glyphport.dataclasses.transformer.DefaultPhone1ToPhone2aPlusLightDataTransformer
import com.sebiai.glyphport.dataclasses.transformer.DefaultPhone1ToPhone3LightDataTransformer
import com.sebiai.glyphport.dataclasses.transformer.DefaultPhone1ToPhone3aAndProLightDataTransformer
import com.sebiai.glyphport.dataclasses.transformer.DefaultPhone2ToPhone1LightDataTransformer

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
            // Phone (1) to X
            LightDataTransformerCollection(
                handles = PhoneModel.PHONE1,
                outputs = PhoneModel.PHONE2,
                transformers = listOf(
                    DefaultPhone1ToPhone2LightDataTransformer(),
                )
            ),
            LightDataTransformerCollection(
                handles = PhoneModel.PHONE1,
                outputs = PhoneModel.PHONE2A,
                transformers = listOf(
                    DefaultPhone1ToPhone2aLightDataTransformer()
                )
            ),
            LightDataTransformerCollection(
                handles = PhoneModel.PHONE1,
                outputs = PhoneModel.PHONE2A_PLUS,
                transformers = listOf(
                    DefaultPhone1ToPhone2aPlusLightDataTransformer()
                )
            ),
            LightDataTransformerCollection(
                handles = PhoneModel.PHONE1,
                outputs = PhoneModel.PHONE3A_AND_PRO,
                transformers = listOf(
                    DefaultPhone1ToPhone3aAndProLightDataTransformer()
                )
            ),
            LightDataTransformerCollection(
                handles = PhoneModel.PHONE1,
                outputs = PhoneModel.PHONE3,
                transformers = listOf(
                    DefaultPhone1ToPhone3LightDataTransformer()
                )
            ),
            // Phone (2) to X
            LightDataTransformerCollection(
                handles = PhoneModel.PHONE2,
                outputs = PhoneModel.PHONE1,
                transformers = listOf(
                    DefaultPhone2ToPhone1LightDataTransformer(),
                )
            )
            // TODO: MORE TRANSFORMERS
        )
    }
}