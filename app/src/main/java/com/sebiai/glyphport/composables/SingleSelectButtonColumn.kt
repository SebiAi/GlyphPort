package com.sebiai.glyphport.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
                text = buttonConfig.text,
                description = buttonConfig.description,
                selected = index == selectedIndex,
                onClick = { onClick(index) }
            )
        }
    }
}

@Composable
private fun SingleSelectButton(
    text: String,
    description: String?,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = if (selected) ButtonDefaults.buttonColors() else
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
        )

    RoundedLargeButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = colors
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = text,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            description?.let {
                Text(
                    style = MaterialTheme.typography.titleSmall,
                    color = LocalContentColor.current.copy(alpha = 0.65f),
                    text = description,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun SingleSelectButtonColumnPreview() {
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
private fun SingleSelectButtonColumnWithSelectionPreview() {
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
private fun SingleSelectButtonColumnWithDescriptionPreview() {
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
private fun SingleSelectButtonColumnWithDescriptionAndSelectionPreview() {
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

