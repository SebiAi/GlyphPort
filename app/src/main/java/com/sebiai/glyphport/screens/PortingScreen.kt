package com.sebiai.glyphport.screens

import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.composables.PortingInfoTable
import com.sebiai.glyphport.composables.RoundedLargeButton
import com.sebiai.glyphport.compositionsSaveDirectory
import com.sebiai.glyphport.dataclasses.Composition
import com.sebiai.glyphport.dataclasses.CompositionMetadataWriter
import com.sebiai.glyphport.dataclasses.DecodedCompositionMetadata
import com.sebiai.glyphport.dataclasses.LightDataTransformer
import com.sebiai.glyphport.dataclasses.compositionPreviewObject
import com.sebiai.glyphport.dataclasses.transformer.DefaultPhone1ToPhone2LightDataTransformer
import com.sebiai.glyphport.getAppNameWithMajorVersion
import com.sebiai.glyphport.getFileName
import com.sebiai.glyphport.screenPaddingModifier
import com.sebiai.glyphport.tryDeleteMediaStoreFile
import com.sebiai.glyphport.ui.theme.GlyphPortTheme
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun PortingScreen(
    modifier: Modifier = Modifier,
    composition: Composition,
    transformer: LightDataTransformer,
    userChooseTransformer: Boolean,
    onPortingSuccess: (Uri) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var portingInProgress by rememberSaveable { mutableStateOf(false) }

    var showErrorOccurredDialog by rememberSaveable { mutableStateOf(false) }
    var errorOccurredDialogReason by rememberSaveable { mutableStateOf("") }

    // Dialog for invalid compositions
    if (showErrorOccurredDialog) {
        AlertDialog(
            onDismissRequest = { showErrorOccurredDialog = false },
            title = { Text(text = stringResource(R.string.porting_failed_dialog_title)) },
            text = {
                Text(
                    text = errorOccurredDialogReason
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { showErrorOccurredDialog = false }
                ) { Text(text = stringResource(R.string.ok_confirmation)) }
            }
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PortingInfoTable(
            modifier = Modifier.verticalScroll(
                state = rememberScrollState()
            ),
            composition = composition,
            portingTarget = transformer.outputs,
            transformerName = transformer.getName(LocalContext.current),
            showTransformerRow = userChooseTransformer
        )
        Spacer(modifier = Modifier.height(18.dp))
        PortButton(
            portingInProgress = portingInProgress,
            onClick = {
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

                    // https://developer.android.com/training/data-storage/shared/media#add-item
                    val audioCollection = MediaStore.Audio.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL_PRIMARY
                    )

                    val oldFile = File(getFileName(context.contentResolver, composition.uri))
                    val newFileName = "${oldFile.nameWithoutExtension}_${context.getString(R.string.file_name_ported_to_phone_model, transformer.outputs.phoneName)}-${transformer.getName(context)}.${oldFile.extension}"
                    val newCompositionDetails = ContentValues().apply {
                        // If the file already exists, it will automatically get a suffix - yay
                        // At least until some point - after 32 files it throws when going from pending
                        // to normal
                        put(MediaStore.Audio.Media.DISPLAY_NAME, newFileName)
                        // put(MediaStore.Audio.Media.CONTENT_TYPE, "audio/ogg") // Not sure about this one
                        put(MediaStore.Audio.Media.IS_RINGTONE, 1)
                        put(MediaStore.Audio.Media.ALBUM, newMetadata.album)
                        put(MediaStore.Audio.Media.COMPOSER, newMetadata.composer)
                        put(MediaStore.Audio.Media.IS_PENDING, 1)
                        put(MediaStore.Audio.Media.MIME_TYPE, "audio/ogg")
                        put(MediaStore.Audio.Media.RELATIVE_PATH, compositionsSaveDirectory)
                        put(MediaStore.Audio.Media.TITLE, newMetadata.title)
                    }
                    val destinationUri = context.contentResolver.insert(
                        audioCollection,
                        newCompositionDetails
                    )!!

                    try {
                        val success = CompositionMetadataWriter(
                            audioFile = composition.uri,
                            outputFile = destinationUri
                        ).write(
                            context = context,
                            metadata = newMetadata.encode()
                        )
                        if (!success) {
                            showErrorOccurredDialog = true
                            errorOccurredDialogReason =
                                context.getString(R.string.porting_general_writing_failed_dialog_reason)
                        } else {
                            newCompositionDetails.clear()
                            newCompositionDetails.put(MediaStore.Audio.Media.IS_PENDING, 0)
                            try {
                                context.contentResolver.update(
                                    destinationUri,
                                    newCompositionDetails,
                                    null,
                                    null
                                )
                            } catch (e: IllegalStateException) {
                                if (
                                    e.message == null ||
                                    "Failed to build unique file" !in e.message!!
                                ) throw e
                                showErrorOccurredDialog = true
                                errorOccurredDialogReason = context.getString(
                                    R.string.porting_creating_unique_file_failed_dialog_reason,
                                    compositionsSaveDirectory,
                                    newFileName
                                )
                            }
                        }
                    } finally {
                        portingInProgress = false
                    }
                    if (!showErrorOccurredDialog) {
                        onPortingSuccess(destinationUri)
                    } else {
                        tryDeleteMediaStoreFile(context.contentResolver, destinationUri)
                    }
                }
            }
        )
    }
}

@Composable
private fun PortButton(
    onClick: () -> Unit,
    portingInProgress: Boolean
) {
    RoundedLargeButton(
        enabled = !portingInProgress,
        onClick = onClick
    ) {
        AnimatedVisibility(
            visible = portingInProgress,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)) + expandHorizontally(clip = false, animationSpec = tween(durationMillis = 500)),
            exit = shrinkHorizontally(clip = false, animationSpec = tween(durationMillis = 500)) + fadeOut(animationSpec = tween(durationMillis = 500))
        ) {
            Row {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp)
                )
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
            }
        }
        Text(
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(R.string.port_composition_action),
            fontWeight = FontWeight.SemiBold
        )
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
                userChooseTransformer = true,
                onPortingSuccess = {}
            )
        }
    }
}

@Preview
@Composable
private fun PortButtonPreview() {
    GlyphPortTheme {
        Surface {
            PortButton(
                onClick = {},
                portingInProgress = true
            )
        }
    }
}