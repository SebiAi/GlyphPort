package com.sebiai.glyphport.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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

@Composable
fun SingleSelectButton(
    modifier: Modifier = Modifier,
    text: String,
    description: String? = null,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val border = if (selected) BorderStroke(
        2.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    ) else null

    val colors = if (selected) ButtonDefaults.buttonColors() else
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
        )

    Button(
        modifier = modifier,
        onClick = onClick,
        colors = colors,
        shape = RoundedCornerShape(25),
        border = null,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 13.dp)
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
fun SingleSelectButtonPreview() {
    GlyphPortTheme {
        SingleSelectButton(
            text = "Hello World!",
            selected = false,
            onClick = {}
        )
    }
}
@Preview
@Composable
fun SingleSelectButtonSelectedPreview() {
    GlyphPortTheme {
        SingleSelectButton(
            text = "Hello World!",
            selected = true,
            onClick = {}
        )
    }
}
@Preview
@Composable
fun SingleSelectButtonWithDescriptionPreview() {
    GlyphPortTheme {
        SingleSelectButton(
            text = "Hello World!",
            description = "Description is meant to be a longer text describing something.",
            selected = false,
            onClick = {}
        )
    }
}
@Preview
@Composable
fun SingleSelectButtonWithDescriptionAndSelectedPreview() {
    GlyphPortTheme {
        SingleSelectButton(
            text = "Hello World!",
            description = "Description is meant to be a longer text describing something.",
            selected = true,
            onClick = {}
        )
    }
}