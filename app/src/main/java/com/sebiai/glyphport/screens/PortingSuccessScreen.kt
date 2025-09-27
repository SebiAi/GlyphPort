package com.sebiai.glyphport.screens

import android.content.ClipData
import android.content.Intent
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.sebiai.glyphport.R
import com.sebiai.glyphport.composables.CenteredTitleWithSubtitle
import com.sebiai.glyphport.composables.RoundedContentContainer
import com.sebiai.glyphport.compositionsSaveDirectory
import com.sebiai.glyphport.getFileName
import com.sebiai.glyphport.screenPaddingModifier
import com.sebiai.glyphport.ui.theme.GlyphPortTheme

@Composable
fun PortingSuccessScreen(
    modifier: Modifier = Modifier,
    portedCompositionUri: Uri,
    onStartOverButtonClicked: () -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        RoundedContentContainer {
            Column {
                CenteredTitleWithSubtitle(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.ported_successfully_heading),
                    subtitle = stringResource(
                        R.string.ported_successfully_subtitle,
                        compositionsSaveDirectory
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = {
                            val fileName = getFileName(context.contentResolver, portedCompositionUri)
                            val shareCompositionIntent = Intent(Intent.ACTION_SEND).apply {
                                // Set mime type and file
                                type = "audio/ogg"
                                // Using the portedCompositionUri that stems from MediaStore shows
                                // a "random" digit in the share dialog - probably a MediaStore
                                // id.
                                // Try to provide all the hints we can - seems to take no effect
                                putExtra(Intent.EXTRA_STREAM, portedCompositionUri)
                                putExtra(Intent.EXTRA_TITLE, fileName)
                                putExtra(Intent.EXTRA_SUBJECT, fileName)
                                clipData = ClipData.newUri(context.contentResolver, fileName, portedCompositionUri)

                                // Don't think this is needed since it is a MediaStore uri but
                                // better be safe than sorry. Might actually be needed since
                                // only my app has access since it was the creator of the file
                                // without READ_MEDIA_AUDIO or READ_EXTERNAL_STORAGE permission.
                                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            }
                            context.startActivity(Intent.createChooser(shareCompositionIntent,
                                context.getString(
                                    R.string.sharing_composition_picker_title
                                )))
                        }
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = Icons.Outlined.Share,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            style = MaterialTheme.typography.titleLarge,
                            text = stringResource(R.string.share_action)
                        )
                    }
                    Button(
                        onClick = onStartOverButtonClicked
                    ) {
                        Text(
                            style = MaterialTheme.typography.titleLarge,
                            text = stringResource(R.string.port_another_composition),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
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