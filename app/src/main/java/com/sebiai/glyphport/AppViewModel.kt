package com.sebiai.glyphport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebiai.glyphport.data.UserPreferencesRepository
import com.sebiai.glyphport.dataclasses.Composition
import com.sebiai.glyphport.dataclasses.LightDataTransformer
import com.sebiai.glyphport.dataclasses.LightDataTransformerCollection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppState(
    val selectedComposition: Composition? = null,
    val selectedTransformerCollection: LightDataTransformerCollection? = null,
    val selectedTransformer: LightDataTransformer? = null
)

@HiltViewModel
class AppViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    /**
     * State
     */
    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    // Settings States - convert from Flow to StateFlow
    val preferenceCheckForUpdates: StateFlow<Boolean> = userPreferencesRepository.checkForUpdates
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = true
        )
    fun preferenceSetCheckForUpdates(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setCheckForUpdates(enabled)
        }
    }

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
    fun updateSelectedTransformerCollection(transformerCollection: LightDataTransformerCollection) {
        _appState.update { currentState ->
            currentState.copy(
                selectedTransformerCollection = transformerCollection
            )
        }
    }
    fun updateSelectedTransformer(transformer: LightDataTransformer) {
        _appState.update { currentState ->
            currentState.copy(
                selectedTransformer = transformer
            )
        }
    }
    fun clearSelections() {
        _appState.update { currentState ->
            currentState.copy(
                selectedComposition = null,
                selectedTransformerCollection = null,
                selectedTransformer = null
            )
        }
    }
}