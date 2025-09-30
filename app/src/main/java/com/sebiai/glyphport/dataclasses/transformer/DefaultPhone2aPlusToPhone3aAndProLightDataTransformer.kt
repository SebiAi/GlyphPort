package com.sebiai.glyphport.dataclasses.transformer

import com.sebiai.glyphport.PhoneModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultPhone2aPlusToPhone3aAndProLightDataTransformer(ioDispatcher: CoroutineDispatcher = Dispatchers.IO):
    DefaultPhone2aToPhone3aAndProLightDataTransformer(ioDispatcher) {
    override val handles: PhoneModel
        get() = PhoneModel.PHONE2A_PLUS
}