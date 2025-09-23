package com.sebiai.glyphport.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.sebiai.glyphport.ui.theme.GlyphPortTheme

@Composable
fun CenteredTitleWithSubtitle(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.headlineSmall,
            text = title,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            style = MaterialTheme.typography.titleSmall,
            color = LocalContentColor.current.copy(alpha = 0.75f),
            text = subtitle,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CenteredTitleWithSubtitlePreview() {
    GlyphPortTheme {
        CenteredTitleWithSubtitle(
            title = "This is a title",
            subtitle = "This is a subtitle explaining something or reasoning about something"
        )
    }
}
