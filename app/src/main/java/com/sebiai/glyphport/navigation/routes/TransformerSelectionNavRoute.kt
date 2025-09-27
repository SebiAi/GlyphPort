package com.sebiai.glyphport.navigation.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sebiai.glyphport.AppViewModel
import com.sebiai.glyphport.screenPaddingModifier
import com.sebiai.glyphport.screens.TransformerSelectionScreen
import kotlinx.serialization.Serializable

@Serializable
internal object TransformerSelectionNavRoute

fun NavController.navigateToTransformerSelectionScreen() {
    navigate(TransformerSelectionNavRoute)
}

fun NavGraphBuilder.transformerSelectionScreenDestination(
    appViewModel: AppViewModel,
    navigateToPortingScreen: (Boolean) -> Unit,
) {
    composable<TransformerSelectionNavRoute> {
        val appState = appViewModel.appState.collectAsStateWithLifecycle().value

        TransformerSelectionScreen(
            modifier = screenPaddingModifier.fillMaxSize(),
            transformerCollection = appState.selectedTransformerCollection!!,
            onNextButtonClicked = { selectedTransformer ->
                appViewModel.updateSelectedTransformer(selectedTransformer)
                navigateToPortingScreen(true) // User did choose the transformer
            }
        )
    }
}