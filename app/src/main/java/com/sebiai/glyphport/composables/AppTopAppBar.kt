package com.sebiai.glyphport.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sebiai.glyphport.R
import com.sebiai.glyphport.ui.theme.GlyphPortTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    showBackArrow: Boolean,
    onBackAction: () -> Unit,
    showAboutAction: Boolean,
    onAboutAction: () -> Unit,
    showSettingsAction: Boolean,
    onSettingsAction: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title
            )
        },
        navigationIcon = {
            AnimatedVisibility(
                visible = showBackArrow,
                enter = fadeIn() + expandHorizontally(),
                exit = shrinkHorizontally() + fadeOut()
            ) {
                IconButton(
                    onClick = onBackAction,
                    enabled = showBackArrow
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(R.string.content_description_top_app_bar_back_arrow_icon)
                    )
                }
            }
        },
        actions = {
            AnimatedVisibility(
                visible = showSettingsAction,
                enter = fadeIn() + expandHorizontally(),
                exit = shrinkHorizontally() + fadeOut()
            ) {
                IconButton(
                    onClick = onSettingsAction,
                    enabled = showSettingsAction
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = null // TODO: Settings content description
                    )
                }
            }
            AnimatedVisibility(
                visible = showAboutAction,
                enter = fadeIn() + expandHorizontally(),
                exit = shrinkHorizontally() + fadeOut()
            ) {
                IconButton(
                    onClick = onAboutAction,
                    enabled = showAboutAction
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = stringResource(R.string.content_description_top_app_bar_more_options_icon) // TODO: CHANGE!
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun MyTopAppBarDefaultPreview() {
    GlyphPortTheme {
        AppTopAppBar(
            title = "AppBar",
            showBackArrow = false,
            onBackAction = {},
            showAboutAction = false,
            onAboutAction = {},
            showSettingsAction = false,
            onSettingsAction = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun MyTopAppBarWitAllVisiblePreview() {
    GlyphPortTheme {
        AppTopAppBar(
            title = "AppBar",
            showBackArrow = true,
            onBackAction = {},
            showAboutAction = true,
            onAboutAction = {},
            showSettingsAction = true,
            onSettingsAction = {}
        )
    }
}