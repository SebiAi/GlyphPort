package com.sebiai.glyphport.navigation.routes

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sebiai.glyphport.AppViewModel
import com.sebiai.glyphport.screenPaddingModifier
import com.sebiai.glyphport.screens.PortingSuccessScreen
import kotlinx.serialization.Serializable

@Serializable
internal data class PortingSuccessNavRoute(
    val portedCompositionUri: String
)

fun NavController.navigateToPortingSuccessScreenWithPopUp(
    portedCompositionUri: Uri,
    fromRoute: Any
) {
    navigate(
        PortingSuccessNavRoute(
            portedCompositionUri = portedCompositionUri.toString()
        )
    ) {
        popUpTo(fromRoute) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.portingSuccessScreenDestination(
    appViewModel: AppViewModel,
    popUpToStartScreen: () -> Unit
) {
    composable<PortingSuccessNavRoute> { backStackEntry ->
        val navRouteObject: PortingSuccessNavRoute = backStackEntry.toRoute()

        PortingSuccessScreen (
            modifier = screenPaddingModifier.fillMaxSize(),
            portedCompositionUri = navRouteObject.portedCompositionUri.toUri(),
            onStartOverButtonClicked = {
                // Clear selections
                appViewModel.clearSelections()
                // Back to start
                popUpToStartScreen()
            }
        )
    }
}