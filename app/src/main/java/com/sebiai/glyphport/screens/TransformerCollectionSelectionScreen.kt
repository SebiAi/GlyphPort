package com.sebiai.glyphport.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.composables.CenteredTitleWithSubtitle
import com.sebiai.glyphport.composables.EndAlignedSingleTextButtonRow
import com.sebiai.glyphport.composables.RoundedContentContainer
import com.sebiai.glyphport.composables.SelectButtonColumnConfig
import com.sebiai.glyphport.composables.SingleSelectButtonColumn
import com.sebiai.glyphport.currentPhoneModel
import com.sebiai.glyphport.dataclasses.LightDataTransformerRegistry
import com.sebiai.glyphport.screenPaddingModifier
import com.sebiai.glyphport.ui.theme.GlyphPortTheme

@Composable
fun TransformerCollectionSelectionScreen(
    modifier: Modifier = Modifier,
    compositionPhoneModel: PhoneModel,
    onGoBackToStartButtonClicked: () -> Unit,
    onNextButtonClicked: (PhoneModel) -> Unit
) {
    val phoneModelOptions by remember { derivedStateOf { LightDataTransformerRegistry.getSupportedConversions(compositionPhoneModel) } }
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(phoneModelOptions.indexOf(currentPhoneModel()))
    }

    Box(
        modifier = modifier
    ) {
        if (phoneModelOptions.isEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CenteredTitleWithSubtitle(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.no_transformer_groups_info_heading),
                    subtitle = stringResource(R.string.no_transformer_groups_info_subtitle, compositionPhoneModel.phoneName)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onGoBackToStartButtonClicked
                ) {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = stringResource(R.string.return_to_beginning)
                    )
                }
            }
        } else {
            RoundedContentContainer(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = stringResource(R.string.transformer_group_selection_heading)
                )
                Spacer(
                    modifier = Modifier.height(18.dp)
                )
                SingleSelectButtonColumn(
                    options = phoneModelOptions.map { SelectButtonColumnConfig(it.phoneName) },
                    selectedIndex = selectedIndex,
                    onClick = { selectedIndex = it }
                )
            }
            EndAlignedSingleTextButtonRow(
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                text = stringResource(R.string.button_next),
                onClick = { onNextButtonClicked(phoneModelOptions.elementAt(selectedIndex)) },
                enabled = selectedIndex >= 0
            )
        }
    }
}

@Preview
@Composable
private fun TransformerCollectionSelectionPreview() {
    GlyphPortTheme {
        Surface {
            TransformerCollectionSelectionScreen(
                modifier = screenPaddingModifier
                    .fillMaxSize(),
                compositionPhoneModel = PhoneModel.PHONE1,
                onGoBackToStartButtonClicked = {},
                onNextButtonClicked = {}
            )
        }
    }
}

@Preview
@Composable
private fun TransformerCollectionSelectionWithoutDevicesPreview() {
    GlyphPortTheme {
        Surface {
            TransformerCollectionSelectionScreen(
                modifier = screenPaddingModifier
                    .fillMaxSize(),
                compositionPhoneModel = PhoneModel.PHONE2A_PLUS,
                onGoBackToStartButtonClicked = {},
                onNextButtonClicked = {}
            )
        }
    }
}