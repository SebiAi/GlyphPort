package com.sebiai.glyphport.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sebiai.glyphport.ui.theme.GlyphPortTheme

@Composable
fun EndAlignedSingleTextButtonRow(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            enabled = enabled,
            onClick = onClick
        ) {
            Text(
                text = text
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun EndAlignedSingleTextButtonRowPreview() {
    GlyphPortTheme {
        EndAlignedSingleTextButtonRow(
            text = "Text",
            onClick = {}
        )
    }
}