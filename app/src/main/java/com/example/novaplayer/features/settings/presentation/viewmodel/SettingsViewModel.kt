package com.example.novaplayer.features.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.novaplayer.features.settings.domain.usecase.ObserveThemeUseCase
import com.example.novaplayer.features.settings.domain.usecase.SetThemeUseCase
import com.example.novaplayer.features.settings.presentation.contract.SettingsContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.features.settings.domain.model.ThemeMode
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val observeThemeUseCase: ObserveThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase
): ViewModel()
{
    private val _uiState = MutableStateFlow(
        SettingsContract.UiState()
    )
    val uiState: StateFlow<SettingsContract.UiState> =
        _uiState.asStateFlow()

    init {
        observeTheme()
    }

    fun onAction(action: SettingsContract.UiAction){
        when(action){
            is SettingsContract.UiAction.SetTheme -> {
                setTheme(action.theme)
            }
        }
    }

    private fun observeTheme(){
        viewModelScope.launch {
            observeThemeUseCase().collect { theme ->
                _uiState.update {
                    it.copy(theme = theme)
                }
            }
        }
    }

    private fun setTheme(theme: ThemeMode) {
        viewModelScope.launch {
            setThemeUseCase(theme)
        }
    }
}