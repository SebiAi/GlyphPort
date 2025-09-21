package com.sebiai.glyphport.dataclasses

import android.net.Uri
import com.sebiai.glyphport.PhoneModel

interface Composition {
    val uri: Uri
    val metadata: DecodedCompositionMetadata
    val parsedLightData: List<List<UInt>>
    val phoneModel: PhoneModel

    fun getColsSize(): Int {
        return if (parsedLightData.isEmpty()) 0 else parsedLightData.first().size
    }
}