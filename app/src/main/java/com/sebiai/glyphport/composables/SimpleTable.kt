package com.sebiai.glyphport.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sebiai.glyphport.ui.theme.GlyphPortTheme

data class SimpleTableRowConfig(
    val title: String,
    val value: String,
    val visible: Boolean = true
)

@Composable
fun SimpleTable(
    modifier: Modifier = Modifier,
    values: Array<SimpleTableRowConfig>
) {
    RoundedContentContainer(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = tween(300)
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            values.forEach { config ->
                AnimatedVisibility(config.visible) {
                    SimpleTableRow(
                        title = config.title,
                        value = config.value
                    )
                }
            }
        }
    }
}

@Composable
private fun SimpleTableRow(
    title: String,
    value: String
) {
    val valueColor = LocalContentColor.current.copy(alpha = 0.7f)

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = title,
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier.weight(1f),
            color = valueColor,
            text = value,
            textAlign = TextAlign.End
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SimpleTablePreview() {
    GlyphPortTheme {
        SimpleTable(
            values = arrayOf(
                SimpleTableRowConfig("Name", "Abc123.ogg"),
                SimpleTableRowConfig("For device", "Phone (69)"),
                SimpleTableRowConfig("Another one", "Really long string to test how the width of the table behaves")
            )
        )
    }
}