package com.sebiai.glyphport.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sebiai.glyphport.PhoneModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.dataclasses.Composition
import com.sebiai.glyphport.dataclasses.compositionPreviewObject
import com.sebiai.glyphport.ui.theme.GlyphPortTheme

@Composable
fun PortingInfoTable(
    modifier: Modifier = Modifier,
    composition: Composition,
    portingTarget: PhoneModel,
    transformerName: String,
    showTransformerRow: Boolean
) {
    SimpleTable(
        modifier = modifier,
        values = arrayOf(
            SimpleTableRowConfig(
                title = stringResource(R.string.file_title_table_heading),
                value = composition.metadata.title
            ),
            SimpleTableRowConfig(
                title = stringResource(R.string.file_made_for_phone_table_heading),
                value = composition.phoneModel.phoneName
            ),
            SimpleTableRowConfig(
                title = stringResource(R.string.port_to_table_heading),
                value = portingTarget.phoneName
            ),
            SimpleTableRowConfig(
                title = stringResource(R.string.port_mode_table_heading),
                value = transformerName,
                visible = showTransformerRow
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PortingInfoTablePreview() {
    GlyphPortTheme {
        PortingInfoTable(
            composition = compositionPreviewObject,
            portingTarget = PhoneModel.PHONE2,
            transformerName = stringResource(R.string.default_light_data_transformer_name),
            showTransformerRow = true
        )
    }
}