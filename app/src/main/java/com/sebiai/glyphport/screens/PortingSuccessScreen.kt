package com.sebiai.glyphport.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.sebiai.glyphport.R
import com.sebiai.glyphport.composables.CenteredTitleWithSubtitle
import com.sebiai.glyphport.getFilePathForLocalMediaStore
import com.sebiai.glyphport.screenPaddingModifier
import com.sebiai.glyphport.ui.theme.GlyphPortTheme

@Composable
fun PortingSuccessScreen(
    modifier: Modifier = Modifier,
    portedCompositionUri: Uri,
    onStartOverButtonClicked: () -> Unit,
) {
    val context = LocalContext.current
    val portedCompositionFilePath by rememberSaveable {
        mutableStateOf(
            getFilePathForLocalMediaStore(
                context,
                portedCompositionUri
            ).removePrefix("/storage/emulated/0/")
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CenteredTitleWithSubtitle(
            modifier = Modifier.fillMaxWidth(),
            title = "Ported successfully!",
            subtitle = "The ported composition was saved in\n$portedCompositionFilePath"
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedButton(
                onClick = onStartOverButtonClicked
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Outlined.Share,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = "Share"
                )
            }
            Button(
                onClick = onStartOverButtonClicked
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = stringResource(R.string.start_over),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview
@Composable
private fun PortingSuccessScreenPreview() {
    GlyphPortTheme {
        Surface {
            PortingSuccessScreen(
                modifier = screenPaddingModifier
                    .fillMaxSize(),
                portedCompositionUri = "content://".toUri(),
                onStartOverButtonClicked = {}
            )
        }
    }
}