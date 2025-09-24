package com.sebiai.glyphport.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.dataclasses.Composition
import com.sebiai.glyphport.dataclasses.compositionPreviewObject
import com.sebiai.glyphport.getFileName
import com.sebiai.glyphport.ui.theme.GlyphPortTheme

@Composable
fun CompositionInfoTable(
    modifier: Modifier = Modifier,
    composition: Composition?
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            val valueColor = LocalContentColor.current.copy(alpha = 0.7f)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(0.25f),
                    text = stringResource(R.string.file_name_table_heading),
                    textAlign = TextAlign.Start
                )
                Text(
                    modifier = Modifier.weight(0.75f),
                    color = valueColor,
                    text = composition?.let { getFileName(LocalContext.current, it.uri) } ?: "-",
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(0.25f),
                    text = stringResource(R.string.file_made_for_phone_table_heading),
                    textAlign = TextAlign.Start
                )
                Text(
                    modifier = Modifier.weight(0.75f),
                    color = valueColor,
                    text = composition?.phoneModel?.phoneName ?: "-",
                    textAlign = TextAlign.End
                )
            }
            val showColumnsRow = composition?.phoneModel == PhoneModel.PHONE1
            AnimatedVisibility(
                visible = showColumnsRow
            ) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.weight(0.25f),
                            text = stringResource(R.string.file_number_of_columns_heading),
                            textAlign = TextAlign.Start
                        )
                        Text(
                            modifier = Modifier.weight(0.75f),
                            color = valueColor,
                            text = if (showColumnsRow) composition.lightData.columns.toString() else String(),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CompositionInfoTablePreview() {
    GlyphPortTheme {
        CompositionInfoTable(
            composition = compositionPreviewObject
        )
    }
}