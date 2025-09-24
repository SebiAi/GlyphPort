package com.sebiai.glyphport.navigation.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.sebiai.glyphport.AppViewModel
import com.sebiai.glyphport.dataclasses.LightDataTransformer
import com.sebiai.glyphport.dataclasses.LightDataTransformerCollection
import com.sebiai.glyphport.dataclasses.LightDataTransformerRegistry
import com.sebiai.glyphport.screenPaddingModifier
import com.sebiai.glyphport.screens.TransformerCollectionSelectionScreen
import kotlinx.serialization.Serializable

@Serializable
internal object TransformerCollectionSelectionNavRoute

fun NavController.navigateToTransformerCollectionSelectionScreen() {
    navigate(TransformerCollectionSelectionNavRoute)
}

fun NavGraphBuilder.transformerCollectionSelectionScreenDestination(
    appViewModel: AppViewModel,
    popUpToStartScreen: () -> Unit,
    navigateToPortingScreen: () -> Unit,
    navigateToTransformerSelectionScreen: () -> Unit
) {
    composable<TransformerCollectionSelectionNavRoute> {
        val appState = appViewModel.appState.collectAsStateWithLifecycle().value

        TransformerCollectionSelectionScreen(
            modifier = screenPaddingModifier.fillMaxSize(),
            compositionPhoneModel = appState.selectedComposition!!.phoneModel,
            onGoBackToStartButtonClicked = popUpToStartScreen,
            onNextButtonClicked = { targetPhoneModel ->
                // If we get a targetPhoneModel we can be sure that there is at least one
                // transformer group because of the logic in TransformerCollectionSelectionScreen
                val transformerCollection = LightDataTransformerRegistry.getCollection(
                    appState.selectedComposition.phoneModel,
                    targetPhoneModel
                )!!
                if (transformerCollection.transformers.size <= 1) {
                    appViewModel.updateSelectedTransformer(transformerCollection.transformers.first())
                    navigateToPortingScreen()
                }
                else {
                    appViewModel.updateSelectedTransformerCollection(transformerCollection)
                    navigateToTransformerSelectionScreen()
                }
            }
        )
    }
}