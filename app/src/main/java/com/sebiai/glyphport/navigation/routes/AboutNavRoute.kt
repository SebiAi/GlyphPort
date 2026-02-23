package com.sebiai.glyphport.navigation.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sebiai.glyphport.screenPaddingModifier
import com.sebiai.glyphport.screens.AboutScreen
import kotlinx.serialization.Serializable

@Serializable
internal object AboutNavRoute

fun NavController.navigateToAboutScreen() {
    navigate(AboutNavRoute)
}

fun NavGraphBuilder.aboutScreenDestination() {
    composable<AboutNavRoute> { backStackEntry ->
        AboutScreen(
            modifier = screenPaddingModifier.fillMaxSize()
        )
    }
}