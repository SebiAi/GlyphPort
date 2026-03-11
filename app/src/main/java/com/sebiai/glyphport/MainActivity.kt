package com.sebiai.glyphport

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.sebiai.glyphport.composables.AppTopAppBar
import com.sebiai.glyphport.navigation.AppNavHost
import com.sebiai.glyphport.navigation.getTitleForRoute
import com.sebiai.glyphport.navigation.routes.AboutNavRoute
import com.sebiai.glyphport.navigation.routes.PortingNavRoute
import com.sebiai.glyphport.navigation.routes.StartNavRoute
import com.sebiai.glyphport.navigation.routes.navigateToAboutScreen
import com.sebiai.glyphport.ui.theme.GlyphPortTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GlyphPortTheme {
                MainActivityContent()
            }
        }
    }
}

@Composable
fun MainActivityContent(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = hiltViewModel()
) {
    // Navigation
    val navController = rememberNavController()

    // App bar state
    var appBarShowBackArrow by remember { mutableStateOf(false) }
    var appBarShowAboutAction by remember { mutableStateOf(false) }
    var appBarShowSettingsAction by remember { mutableStateOf(false) }
    var appBarTitle by remember { mutableStateOf("") }

    navController.addOnDestinationChangedListener { controller, destination, arguments ->
        appBarShowBackArrow = controller.previousBackStackEntry != null


        destination.route?.let { route ->
            // routes have the qualifiedName of the class plus a url like arguments
            // when a data class is used
            val routeQualifiedName = route.substringBefore('/')
            appBarTitle = getTitleForRoute(controller.context, routeQualifiedName)

            appBarShowSettingsAction = when (routeQualifiedName) {
                AboutNavRoute::class.qualifiedName!! -> false
                // TODO: Add settings route
                PortingNavRoute::class.qualifiedName!! -> false
                // TODO: Enable settings icon in app bar
                else -> false
            }
            appBarShowAboutAction = when (routeQualifiedName) {
                AboutNavRoute::class.qualifiedName!! -> false
                // TODO: Add settings route
                PortingNavRoute::class.qualifiedName!! -> false
                else -> true
            }

        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = appBarTitle,
                showBackArrow = appBarShowBackArrow,
                onBackAction = {
                    navController.popBackStack()
                },
                showAboutAction = appBarShowAboutAction,
                onAboutAction = { navController.navigateToAboutScreen() },
                showSettingsAction = appBarShowSettingsAction,
                onSettingsAction = { /* TODO: Implement */ }
            )
        }
    ) { innerPadding ->
        AppNavHost(
            modifier = modifier.padding(innerPadding),
            navController = navController,
            startDestination = StartNavRoute,
            appViewModel
        )
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun MainActivityPreview() {
    MainActivityContent()
}