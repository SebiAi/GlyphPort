package com.sebiai.glyphport.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onBackArrowPressed: () -> Unit,
    showMoreOptions: Boolean,
    onOptionAboutPressed: () -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

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
                    onClick = onBackArrowPressed,
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
                visible = showMoreOptions,
                enter = fadeIn() + expandHorizontally(),
                exit = shrinkHorizontally() + fadeOut()
            ) {
                IconButton(
                    onClick = { dropdownExpanded = !dropdownExpanded },
                    enabled = showMoreOptions
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = stringResource(R.string.content_description_top_app_bar_more_options_icon)
                    )
                    AppDropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        onOptionAboutPressed = onOptionAboutPressed
                    )
                }
            }
        }
    )
}

@Composable
private fun AppDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onOptionAboutPressed: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.about)
                )
            },
            onClick = {
                onDismissRequest()
                onOptionAboutPressed()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun MyTopAppBarDefaultPreview() {
    GlyphPortTheme {
        AppTopAppBar(
            title = "AppBar",
            showBackArrow = false,
            onBackArrowPressed = {},
            showMoreOptions = false,
            onOptionAboutPressed = {}
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
            onBackArrowPressed = {},
            showMoreOptions = true,
            onOptionAboutPressed = {}
        )
    }
}