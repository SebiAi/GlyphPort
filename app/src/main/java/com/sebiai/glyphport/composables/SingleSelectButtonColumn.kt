package com.sebiai.glyphport.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sebiai.glyphport.ui.theme.GlyphPortTheme

data class SelectButtonColumnConfig(
    val text: String,
    val description: String? = null
)

@Composable
fun SingleSelectButtonColumn(
    modifier: Modifier = Modifier,
    options: List<SelectButtonColumnConfig>,
    selectedIndex: Int,
    onClick: (Int) -> Unit,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        options.forEachIndexed { index, buttonConfig ->
            SingleSelectButton(
                modifier = Modifier.fillMaxWidth(),
                text = buttonConfig.text,
                description = buttonConfig.description,
                selected = index == selectedIndex,
                onClick = { onClick(index) }
            )
        }
    }
}

@Preview
@Composable
fun SingleSelectButtonColumnPreview() {
    GlyphPortTheme {
        SingleSelectButtonColumn(
            options = listOf(
                SelectButtonColumnConfig("Option1"),
                SelectButtonColumnConfig("Option2"),
                SelectButtonColumnConfig("Option3")
            ),
            selectedIndex = -1,
            onClick = {}
        )
    }
}
@Preview
@Composable
fun SingleSelectButtonColumnWithSelectionPreview() {
    GlyphPortTheme {
        SingleSelectButtonColumn(
            options = listOf(
                SelectButtonColumnConfig("Option1"),
                SelectButtonColumnConfig("Option2"),
                SelectButtonColumnConfig("Option3")
            ),
            selectedIndex = 0,
            onClick = {}
        )
    }
}
@Preview
@Composable
fun SingleSelectButtonColumnWithDescriptionPreview() {
    GlyphPortTheme {
        SingleSelectButtonColumn(
            options = listOf(
                SelectButtonColumnConfig("Option1", "Description of Option1"),
                SelectButtonColumnConfig("Option2", "Description of Option2"),
                SelectButtonColumnConfig("Option3", "Description of Option3")
            ),
            selectedIndex = -1,
            onClick = {}
        )
    }
}
@Preview
@Composable
fun SingleSelectButtonColumnWithDescriptionAndSelectionPreview() {
    GlyphPortTheme {
        SingleSelectButtonColumn(
            options = listOf(
                SelectButtonColumnConfig("Option1", "Description of Option1"),
                SelectButtonColumnConfig("Option2", "Description of Option2"),
                SelectButtonColumnConfig("Option3", "Description of Option3")
            ),
            selectedIndex = 0,
            onClick = {}
        )
    }
}

