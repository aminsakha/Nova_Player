package com.example.novaplayer.features.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.core.locale.AppLocaleManager
import com.example.novaplayer.features.settings.domain.model.AppLanguage
import com.example.novaplayer.features.settings.domain.model.ThemeMode
import com.example.novaplayer.features.settings.domain.usecase.ObserveLanguageUseCase
import com.example.novaplayer.features.settings.domain.usecase.ObserveThemeUseCase
import com.example.novaplayer.features.settings.domain.usecase.SetLanguageUseCase
import com.example.novaplayer.features.settings.domain.usecase.SetThemeUseCase
import com.example.novaplayer.features.settings.presentation.contract.SettingsContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val observeThemeUseCase: ObserveThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val observeLanguageUseCase: ObserveLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val appLocaleManager: AppLocaleManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsContract.UiState()
    )

    val uiState: StateFlow<SettingsContract.UiState> =
        _uiState.asStateFlow()

    init {
        observeTheme()
        observeLanguage()
    }

    fun onAction(action: SettingsContract.UiAction) {
        when (action) {

            is SettingsContract.UiAction.SetTheme -> {
                setTheme(action.theme)
            }

            is SettingsContract.UiAction.SetLanguage -> {
                setLanguage(action.language)
            }
        }
    }

    private fun observeTheme() {
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

    private fun observeLanguage() {
        viewModelScope.launch {
            observeLanguageUseCase().collect { language ->
                _uiState.update {
                    it.copy(language = language)
                }
            }
        }
    }

    private fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            setLanguageUseCase(language)
        }
        appLocaleManager.setLanguage(language)
    }
}