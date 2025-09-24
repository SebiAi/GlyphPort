package com.sebiai.glyphport.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AudioFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.sebiai.glyphport.R
import com.sebiai.glyphport.composables.CompositionInfoTable
import com.sebiai.glyphport.composables.EndAlignedSingleTextButtonRow
import com.sebiai.glyphport.composables.RoundedLargeButton
import com.sebiai.glyphport.dataclasses.Composition
import com.sebiai.glyphport.dataclasses.CompositionValidator
import com.sebiai.glyphport.dataclasses.ValidationError
import com.sebiai.glyphport.dataclasses.ValidationResult
import com.sebiai.glyphport.dataclasses.compositionPreviewObject
import com.sebiai.glyphport.screenPaddingModifier
import com.sebiai.glyphport.ui.theme.GlyphPortTheme
import kotlinx.coroutines.launch

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    composition: Composition?,
    updateSelectedComposition: (Composition?) -> Unit,
    onNextButtonClicked: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var showInvalidCompositionDialog by rememberSaveable { mutableStateOf(false) }
    var invalidCompositionDialogReason by rememberSaveable { mutableStateOf("") }

    val pickCompositionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { it?.let {
        Log.d("CompositionSelector", "Picked file: ${it.path}")
        lifecycleOwner.lifecycleScope.launch {
            val result = CompositionValidator.validate(context, it)
            when (result) {
                is ValidationResult.Failure -> {
                    Log.d("CompositionSelector", "Composition validation failed: ${result.reason.msg}")
                    // I'm not sure if it is ok to touch functions and variables outside of the
                    // coroutine but it works and the IDE is not screaming at me
                    updateSelectedComposition(null)
                    invalidCompositionDialogReason = when (val reason = result.reason) {
                        is ValidationError.CompositionFormatTooNew -> context.getString(R.string.composition_validation_composition_format_too_new)
                        is ValidationError.Extension -> context.getString(R.string.composition_validation_invalid_extension)
                        is ValidationError.IO -> context.getString(R.string.composition_validation_io_error)
                        is ValidationError.InvalidMetadata -> context.getString(R.string.composition_validation_invalid_metadata)
                        is ValidationError.MissingMetadata -> context.getString(R.string.composition_validation_invalid_codec_or_missing_metadata)
                        is ValidationError.UnknownOrUnsupportedPhoneModel -> context.getString(R.string.composition_validation_unknown_or_unsupported_phone_model, reason.build)
                    }
                    showInvalidCompositionDialog = true
                }
                is ValidationResult.Success -> {
                    Log.d("CompositionSelector", "Composition validation passed")
                    updateSelectedComposition(result.composition)
                }
            }
        }
    } }

    // Dialog for invalid compositions
    if (showInvalidCompositionDialog) {
        AlertDialog(
            onDismissRequest = { showInvalidCompositionDialog = false },
            title = { Text(text = stringResource(R.string.invalid_composition_dialog_title)) },
            text = {
                Text(
                    text = invalidCompositionDialogReason
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { showInvalidCompositionDialog = false }
                ) { Text(text = stringResource(R.string.ok_confirmation)) }
            }
        )
    }

    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RoundedLargeButton(
                onClick = {
                    pickCompositionLauncher.launch("audio/ogg")
                }
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Outlined.AudioFile,
                    contentDescription = null
                )
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                Text(
                    style = MaterialTheme.typography.headlineMedium,
                    text = stringResource(R.string.button_select_composition),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                style = MaterialTheme.typography.labelLarge,
                color = LocalContentColor.current.copy(alpha = 0.6f),
                text = stringResource(R.string.supported_formats_hint),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            CompositionInfoTable(
                composition = composition
            )
        }
        EndAlignedSingleTextButtonRow(
            text = stringResource(R.string.button_next),
            onClick = onNextButtonClicked,
            enabled = composition != null
        )
    }
}

@Preview
@Composable
private fun StartScreenPreview() {
    GlyphPortTheme {
        Surface {
            StartScreen(
                modifier = screenPaddingModifier
                    .fillMaxSize(),
                composition = compositionPreviewObject,
                updateSelectedComposition = {},
                onNextButtonClicked = {}
            )
        }
    }
}