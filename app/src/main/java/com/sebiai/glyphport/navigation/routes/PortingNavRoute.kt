package com.sebiai.glyphport.navigation.routes

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sebiai.glyphport.AppViewModel
import com.sebiai.glyphport.screenPaddingModifier
import com.sebiai.glyphport.screens.PortingScreen
import kotlinx.serialization.Serializable

@Serializable
internal data class PortingNavRoute(
    val userChooseTransformer: Boolean
)

fun NavController.navigateToPortingScreen(userChooseTransformer: Boolean) {
    navigate(
        PortingNavRoute(
            userChooseTransformer = userChooseTransformer
        )
    )
}

fun NavGraphBuilder.portingScreenDestination(
    appViewModel: AppViewModel,
    popUpToPortingSuccessScreen: (Uri) -> Unit
) {
    composable<PortingNavRoute> { backStackEntry ->
        val navRouteObject: PortingNavRoute = backStackEntry.toRoute()
        val appState = appViewModel.appState.collectAsStateWithLifecycle().value

        PortingScreen(
            modifier = screenPaddingModifier.fillMaxSize(),
            composition = appState.selectedComposition!!,
            transformer = appState.selectedTransformer!!,
            userChooseTransformer = navRouteObject.userChooseTransformer,
            onPortingSuccess = popUpToPortingSuccessScreen
        )
    }
}