package com.sebiai.glyphport.navigation.routes

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sebiai.glyphport.AppViewModel
import com.sebiai.glyphport.screenPaddingModifier
import com.sebiai.glyphport.screens.PortingScreen
import kotlinx.serialization.Serializable

@Serializable
internal object PortingNavRoute

fun NavController.navigateToPortingScreen() {
    navigate(PortingNavRoute)
}

fun NavGraphBuilder.portingScreenDestination(
    appViewModel: AppViewModel,
    popUpToPortingSuccessScreen: (Uri) -> Unit
) {
    composable<PortingNavRoute> {
        val appState = appViewModel.appState.collectAsStateWithLifecycle().value

        PortingScreen(
            modifier = screenPaddingModifier.fillMaxSize(),
            composition = appState.selectedComposition!!,
            transformer = appState.selectedTransformer!!,
            onPortingSuccess = popUpToPortingSuccessScreen
        )
    }
}