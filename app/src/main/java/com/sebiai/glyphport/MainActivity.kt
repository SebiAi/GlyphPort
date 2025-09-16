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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.sebiai.glyphport.composables.AppTopAppBar
import com.sebiai.glyphport.navigation.AppNavHost
import com.sebiai.glyphport.navigation.getTitleForRoute
import com.sebiai.glyphport.navigation.routes.StartNavRoute
import com.sebiai.glyphport.ui.theme.GlyphPortTheme

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
    appViewModel: AppViewModel = viewModel()
) {
    // Navigation
    val navController = rememberNavController()

    // App bar state
    var appBarShowBackArrow by remember { mutableStateOf(false) }
    var appBarTitle by remember { mutableStateOf("") }

    navController.addOnDestinationChangedListener { controller, destination, arguments ->
        appBarShowBackArrow = controller.previousBackStackEntry != null
        destination.route?.let { route ->
            appBarTitle = getTitleForRoute(controller.context, route)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = appBarTitle,
                showBackArrow = appBarShowBackArrow,
                onBackArrowPressed = {
                    navController.popBackStack()
                }
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