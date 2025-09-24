package com.sebiai.glyphport.navigation

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.sebiai.glyphport.AppViewModel
import com.sebiai.glyphport.R
import com.sebiai.glyphport.navigation.routes.PortingNavRoute
import com.sebiai.glyphport.navigation.routes.PortingSuccessNavRoute
import com.sebiai.glyphport.navigation.routes.StartNavRoute
import com.sebiai.glyphport.navigation.routes.TransformerCollectionSelectionNavRoute
import com.sebiai.glyphport.navigation.routes.navigateToPortingScreen
import com.sebiai.glyphport.navigation.routes.navigateToPortingSuccessScreenWithPopUp
import com.sebiai.glyphport.navigation.routes.navigateToStartScreen
import com.sebiai.glyphport.navigation.routes.navigateToStartScreenWithPopUp
import com.sebiai.glyphport.navigation.routes.navigateToTransformerCollectionSelectionScreen
import com.sebiai.glyphport.navigation.routes.portingScreenDestination
import com.sebiai.glyphport.navigation.routes.portingSuccessScreenDestination
import com.sebiai.glyphport.navigation.routes.startScreenDestination
import com.sebiai.glyphport.navigation.routes.transformerCollectionSelectionScreenDestination

fun getTitleForRoute(context: Context, route: String): String {
    // routes have the qualifiedName of the class plus a url like arguments
    // when a data class is used
    val routeQualifiedName = route.substringBefore('/')
    val titleRes = when (routeQualifiedName) {
        StartNavRoute::class.qualifiedName!! -> R.string.app_name
        TransformerCollectionSelectionNavRoute::class.qualifiedName!! -> R.string.top_app_bar_title_transformer_group_selection_screen
        PortingNavRoute::class.qualifiedName!! -> R.string.top_app_bar_title_porting_screen
        PortingSuccessNavRoute::class.qualifiedName!! -> R.string.app_name
        else -> null
    }
    return titleRes?.let { context.getString(titleRes) }?: ""
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: Any,
    appViewModel: AppViewModel
) {
    val layoutDirectionFactor = when (LocalLayoutDirection.current) {
        LayoutDirection.Ltr -> 1
        LayoutDirection.Rtl -> -1
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { layoutDirectionFactor * it / 2 }) +
                    fadeIn(animationSpec = tween(400))
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { layoutDirectionFactor * -it / 2 }) +
                    fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { layoutDirectionFactor * -it / 2 }) +
                    fadeIn(animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { layoutDirectionFactor * it / 2 }) +
                    fadeOut(animationSpec = tween(400))
        },
    ) {
        startScreenDestination(
            appViewModel = appViewModel,
            navigateToTransformerCollectionScreen = navController::navigateToTransformerCollectionSelectionScreen
        )
        transformerCollectionSelectionScreenDestination(
            appViewModel = appViewModel,
            popUpToStartScreen = {
                navController.navigateToStartScreenWithPopUp(
                    fromRoute = StartNavRoute
                )
            },
            navigateToPortingScreen = navController::navigateToPortingScreen,
            navigateToTransformerSelectionScreen = {
                // TODO: navigateToTransformerSelectionScreen
                Log.d("Navigation", "navigateToTransformerSelectionScreen")
            }
        )
        // TODO: This navigation needs work
        portingScreenDestination(
            appViewModel = appViewModel,
            popUpToPortingSuccessScreen = {
                navController.navigateToPortingSuccessScreenWithPopUp(
                    portedCompositionUri = it,
                    fromRoute = StartNavRoute
                )
            }
        )
        portingSuccessScreenDestination(
            appViewModel = appViewModel,
            popUpToStartScreen = {
                navController.navigateToStartScreen()
            },
        )
    }
}