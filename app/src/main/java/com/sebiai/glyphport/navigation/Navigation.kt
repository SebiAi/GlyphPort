package com.sebiai.glyphport.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        startScreenDestination(
            appViewModel = appViewModel,
            onNextButtonClicked = { /* TODO: Navigate to next screen */ }
        )
    }
}