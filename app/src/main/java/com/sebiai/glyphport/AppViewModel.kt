package com.sebiai.glyphport

import androidx.lifecycle.ViewModel
import com.sebiai.glyphport.dataclasses.Composition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppState(
    val selectedComposition: Composition? = null,
)

class AppViewModel : ViewModel() {
    /**
     * State
     */
    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    /*
     * Business logic
     */
    fun updateSelectedComposition(composition: Composition?) {
        _appState.update { currentState ->
            currentState.copy(
                selectedComposition = composition
            )
        }
    }
}