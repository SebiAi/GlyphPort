package com.sebiai.glyphport.navigation.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sebiai.glyphport.AppViewModel
import com.sebiai.glyphport.screenPaddingModifier
import com.sebiai.glyphport.screens.StartScreen
import kotlinx.serialization.Serializable

@Serializable
internal object StartNavRoute

fun NavController.navigateToStartScreen() {
    navigate(StartNavRoute)
}
fun NavController.navigateToStartScreenWithPopUp(
    fromRoute: Any
) {
    navigate(StartNavRoute) {
        popUpTo(fromRoute) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.startScreenDestination(
    appViewModel: AppViewModel,
    navigateToTransformerCollectionScreen: () -> Unit
) {
    composable<StartNavRoute> {
        val appState = appViewModel.appState.collectAsStateWithLifecycle().value

        StartScreen(
            modifier = screenPaddingModifier.fillMaxSize(),
            composition = appState.selectedComposition,
            updateSelectedComposition = appViewModel::updateSelectedComposition,
            onNextButtonClicked = navigateToTransformerCollectionScreen
        )
    }
}