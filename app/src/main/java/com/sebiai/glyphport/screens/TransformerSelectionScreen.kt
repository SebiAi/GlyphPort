package com.sebiai.glyphport.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.composables.EndAlignedSingleTextButtonRow
import com.sebiai.glyphport.composables.RoundedContentContainer
import com.sebiai.glyphport.composables.SelectButtonColumnConfig
import com.sebiai.glyphport.composables.SingleSelectButtonColumn
import com.sebiai.glyphport.dataclasses.LightDataTransformer
import com.sebiai.glyphport.dataclasses.LightDataTransformerCollection
import com.sebiai.glyphport.dataclasses.LightDataTransformerRegistry
import com.sebiai.glyphport.screenPaddingModifier
import com.sebiai.glyphport.ui.theme.GlyphPortTheme

@Composable
fun TransformerSelectionScreen(
    modifier: Modifier = Modifier,
    transformerCollection: LightDataTransformerCollection,
    onNextButtonClicked: (LightDataTransformer) -> Unit
) {
    val context = LocalContext.current
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Column(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            RoundedContentContainer {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = stringResource(R.string.transformer_selection_heading)
                )
                Spacer(
                    modifier = Modifier.height(18.dp)
                )
                SingleSelectButtonColumn(
                    modifier = Modifier.verticalScroll(
                        state = rememberScrollState()
                    ),
                    options = transformerCollection.transformers.map {
                        SelectButtonColumnConfig(
                            text = it.getName(context),
                            description = it.getDescription(context)
                        )
                    },
                    selectedIndex = selectedIndex,
                    onClick = { selectedIndex = it }
                )
            }
        }
        EndAlignedSingleTextButtonRow(
            text = stringResource(R.string.next_action),
            onClick = { onNextButtonClicked(transformerCollection.transformers.elementAt(selectedIndex)) },
            enabled = selectedIndex >= 0
        )
    }
}

@Preview
@Composable
private fun TransformerSelectionPreview() {
    GlyphPortTheme {
        Surface {
            TransformerSelectionScreen(
                modifier = screenPaddingModifier
                    .fillMaxSize(),
                transformerCollection = LightDataTransformerRegistry.getCollection(
                    input = PhoneModel.PHONE1,
                    output = PhoneModel.PHONE2
                )!!,
                onNextButtonClicked = {}
            )
        }
    }
}