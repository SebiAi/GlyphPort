package com.sebiai.glyphport.dataclasses

import android.content.Context
import androidx.annotation.StringRes
import com.sebiai.glyphport.PhoneModel

interface LightDataTransformer {
    @get:StringRes
    val nameStringRes: Int
    @get:StringRes
    val descriptionStringRes: Int

    fun getName(context: Context): String = context.getString(nameStringRes)
    fun getDescription(context: Context): String = context.getString(descriptionStringRes)

    fun transform(lightData: CompositionLightData): CompositionLightData
    fun canHandle(handles: PhoneModel, output: PhoneModel): Boolean
}