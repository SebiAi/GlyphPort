package com.sebiai.glyphport

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val screenPaddingModifier = Modifier.padding(12.dp)

enum class PhoneModel(val phoneName: String, val build: String, val supportedZones: Array<UInt>) {
    PHONE1("Phone (1)", "Spacewar", arrayOf(5u, 15u)),
    PHONE2("Phone (2)", "Pong", arrayOf(33u)),
    PHONE2A("Phone (2a)", "Pacman", arrayOf(26u)),
    PHONE2A_PLUS("Phone (2a) Plus", "PacmanPro", arrayOf(26u)),
    // Only together because there is no difference in the metadata
    PHONE3A_AND_PRO("Phone (3a) / Phone (3a) Pro", "Asteroids", arrayOf(36u)),
    PHONE3("Phone (3)", "Metroid", arrayOf(625u)),
}

/**
 * Get the current nothing phone model the app is running on.
 *
 * @return The phone model or null if the device is not recognized as
 * a Nothing phone.
 */
fun currentPhoneModel(): PhoneModel? {
    return when (Build.MODEL) {
        "A063" -> PhoneModel.PHONE1
        "A065", "AIN065" -> PhoneModel.PHONE2
        "A142" -> PhoneModel.PHONE2A
        "A142P" -> PhoneModel.PHONE2A_PLUS
        "A059", "A059P" -> PhoneModel.PHONE3A_AND_PRO
        "A024" -> PhoneModel.PHONE3
        else -> null
    }
}

fun getFileName(context: Context, uri: Uri): String {
    // https://developer.android.com/training/secure-file-sharing/retrieve-info#RetrieveFileInfo
    return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        cursor.getString(nameIndex)
    }.orEmpty()
}