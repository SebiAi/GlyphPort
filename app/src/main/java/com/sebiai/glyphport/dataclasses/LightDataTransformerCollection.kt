package com.sebiai.glyphport.dataclasses

import com.sebiai.glyphport.PhoneModel

data class LightDataTransformerCollection(
    val handles: PhoneModel,
    val outputs: PhoneModel,
    val transformers: List<LightDataTransformer>
) {
    init {
        require(handles != outputs) {
            "handles must be different than outputs"
        }
        require(transformers.isNotEmpty()){
            "transformers must not be empty"
        }
        require(transformers.all { it.canHandle(handles, outputs) }) {
            "All transformers must match collections phone models (handles, outputs)"
        }
    }
}