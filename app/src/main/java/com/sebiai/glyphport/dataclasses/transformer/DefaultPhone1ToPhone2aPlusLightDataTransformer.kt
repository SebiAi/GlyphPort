package com.sebiai.glyphport.dataclasses.transformer

import com.sebiai.glyphport.PhoneModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultPhone1ToPhone2aPlusLightDataTransformer(ioDispatcher: CoroutineDispatcher = Dispatchers.IO):
    DefaultPhone1ToPhone2aLightDataTransformer(ioDispatcher) {
    override val outputs: PhoneModel
        get() = PhoneModel.PHONE2A_PLUS
}