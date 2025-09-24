package com.sebiai.glyphport.dataclasses

import android.net.Uri
import androidx.core.net.toUri
import com.sebiai.glyphport.PhoneModel

interface Composition {
    val uri: Uri
    val metadata: DecodedCompositionMetadata
    val lightData: CompositionLightData
    val phoneModel: PhoneModel
}

val compositionPreviewObject: Composition = CompositionPreviewImpl()
private class CompositionPreviewImpl(
    override val uri: Uri = "content://".toUri(),
    override val metadata: DecodedCompositionMetadata = DecodedCompositionMetadata(),
    override val lightData: CompositionLightData = CompositionLightData(listOf(listOf(0u, 0u, 0u, 0u, 0u))),
    override val phoneModel: PhoneModel = PhoneModel.PHONE1
) : Composition