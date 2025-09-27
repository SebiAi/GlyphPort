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
fun CompositionInfoTable(
    modifier: Modifier = Modifier,
    composition: Composition?
) {
    val showColumnsRow = composition?.phoneModel == PhoneModel.PHONE1

    SimpleTable(
        modifier = modifier,
        values = arrayOf(
            SimpleTableRowConfig(
                title = stringResource(R.string.file_title_table_heading),
                value = composition?.metadata?.title ?: "-"
            ),
            SimpleTableRowConfig(
                title = stringResource(R.string.file_made_for_phone_table_heading),
                value = composition?.phoneModel?.phoneName ?: "-"
            ),
            SimpleTableRowConfig(
                title = stringResource(R.string.file_number_of_columns_heading),
                value = if (showColumnsRow) composition.lightData.columns.toString() else String(),
                visible = showColumnsRow
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun CompositionInfoTablePreview() {
    GlyphPortTheme {
        CompositionInfoTable(
            composition = compositionPreviewObject
        )
    }
}