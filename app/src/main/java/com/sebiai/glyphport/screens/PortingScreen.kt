package com.sebiai.glyphport.screens

import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.composables.CompositionInfoTable
import com.sebiai.glyphport.dataclasses.Composition
import com.sebiai.glyphport.dataclasses.CompositionMetadata
import com.sebiai.glyphport.dataclasses.CompositionMetadataWriter
import com.sebiai.glyphport.dataclasses.DecodedCompositionMetadata
import com.sebiai.glyphport.dataclasses.LightDataTransformer
import com.sebiai.glyphport.dataclasses.compositionPreviewObject
import com.sebiai.glyphport.dataclasses.transformer.DefaultPhone1ToPhone2LightDataTransformer
import com.sebiai.glyphport.getAppNameWithMajorVersion
import com.sebiai.glyphport.getFileName
import com.sebiai.glyphport.screenPaddingModifier
import com.sebiai.glyphport.ui.theme.GlyphPortTheme
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun PortingScreen(
    modifier: Modifier = Modifier,
    composition: Composition,
    transformer: LightDataTransformer,
    onGoBackToStartButtonClicked: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var portingInProgress by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CompositionInfoTable(
            modifier = Modifier.padding(horizontal = 8.dp),
            composition = composition
        )
        Spacer(modifier = Modifier.height(18.dp))
        Button(
            enabled = !portingInProgress,
            onClick = {
                // TODO: [NOW] Better error and exception handling
                portingInProgress = true

                lifecycleOwner.lifecycleScope.launch {
                    val transformedLightData = transformer.transform(composition.lightData)
                    val newMetadata = DecodedCompositionMetadata(
                        title = composition.metadata.title,
                        album = getAppNameWithMajorVersion(context),
                        composer = "v1-${transformer.outputs.build} Glyph Composer",
                        authorLightData = transformedLightData,
                        custom1 = composition.metadata.custom1,
                        custom2 = if (transformer.outputs == PhoneModel.PHONE1) "5Cols" else "${transformedLightData.columns}Cols"
                    )

                    MediaStore.getExternalVolumeNames(context)
                    // https://developer.android.com/training/data-storage/shared/media#add-item
                    val audioCollection = MediaStore.Audio.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL_PRIMARY
                    )

                    val oldFile = File(getFileName(context, composition.uri))
                    val newFileName = "${oldFile.nameWithoutExtension}_${context.getString(R.string.ported_to_phone_model, transformer.outputs.phoneName)}-${transformer.getName(context)}.${oldFile.extension}"
                    val newCompositionDetails = ContentValues().apply {
                        // If the file already exists, it will automatically get a suffix - yay

                        put(MediaStore.Audio.Media.DISPLAY_NAME, newFileName)
                        // put(MediaStore.Audio.Media.CONTENT_TYPE, "audio/ogg") // Not sure about this one
                        put(MediaStore.Audio.Media.IS_RINGTONE, 1)
                        put(MediaStore.Audio.Media.ALBUM, newMetadata.album)
                        put(MediaStore.Audio.Media.COMPOSER, newMetadata.composer)
                        put(MediaStore.Audio.Media.IS_PENDING, 1)
                        put(MediaStore.Audio.Media.MIME_TYPE, "audio/ogg")
                        put(MediaStore.Audio.Media.RELATIVE_PATH, "${Environment.DIRECTORY_RINGTONES}${File.separator}Compositions")
                        put(MediaStore.Audio.Media.TITLE, newMetadata.title)
                    }
                    val destinationUri = context.contentResolver.insert(
                        audioCollection,
                        newCompositionDetails
                    )!!

                    try {
                        CompositionMetadataWriter(
                            audioFile = composition.uri,
                            outputFile = destinationUri
                        ).write(
                            context = context,
                            metadata = newMetadata.encode()
                        )
                    } finally {
                        newCompositionDetails.clear()
                        newCompositionDetails.put(MediaStore.Audio.Media.IS_PENDING, 0)
                        context.contentResolver.update(
                            destinationUri,
                            newCompositionDetails,
                            null,
                            null
                        )

                        portingInProgress = false
                    }
                }
            }
        ) {
            Text(
                style = MaterialTheme.typography.headlineMedium,
                text = "Port Composition",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview
@Composable
private fun PortingScreenPreview() {
    GlyphPortTheme {
        Surface {
            PortingScreen(
                modifier = screenPaddingModifier
                    .fillMaxSize(),
                composition = compositionPreviewObject,
                transformer = DefaultPhone1ToPhone2LightDataTransformer(),
                onGoBackToStartButtonClicked = {}
            )
        }
    }
}