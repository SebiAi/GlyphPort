package com.sebiai.glyphport.navigation

import android.content.Context
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
import com.sebiai.glyphport.navigation.routes.StartNavRoute
import com.sebiai.glyphport.navigation.routes.startScreenDestination

fun getTitleForRoute(context: Context, route: String): String {
    // routes have the qualifiedName of the class plus a url like arguments
    // when a data class is used
    val routeQualifiedName = route.substringBefore('/')
    val titleRes = when (routeQualifiedName) {
        StartNavRoute::class.qualifiedName!! -> R.string.app_name
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
            onNextButtonClicked = { /* TODO: Navigate to next screen */ }
        )
    }
}