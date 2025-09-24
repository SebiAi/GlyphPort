package com.sebiai.glyphport.dataclasses

import android.content.Context
import androidx.annotation.StringRes
import com.sebiai.glyphport.PhoneModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class LightDataTransformer(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    @get:StringRes
    protected abstract val nameStringRes: Int
    @get:StringRes
    protected abstract val descriptionStringRes: Int
    abstract val handles: PhoneModel
    abstract val outputs: PhoneModel

    fun getName(context: Context): String = context.getString(nameStringRes)
    fun getDescription(context: Context): String = context.getString(descriptionStringRes)

    suspend fun transform(lightData: CompositionLightData): CompositionLightData = withContext(ioDispatcher) {
        return@withContext transformImpl(lightData)
    }
    protected abstract fun transformImpl(lightData: CompositionLightData): CompositionLightData
    fun canHandle(handles: PhoneModel, output: PhoneModel): Boolean {
        return handles == this.handles && output == this.outputs
    }
}