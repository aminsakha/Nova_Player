package com.example.novaplayer.features.settings.presentation.contract

import com.example.novaplayer.features.settings.domain.model.AppLanguage
import com.example.novaplayer.features.settings.domain.model.ThemeMode

object SettingsContract {
    data class UiState(
        val theme: ThemeMode = ThemeMode.SYSTEM,
        val language: AppLanguage = AppLanguage.EN
    )
    sealed interface UiAction{
        data class SetTheme(
            val theme: ThemeMode
        ): UiAction
        data class SetLanguage(
            val language: AppLanguage
        ) : UiAction
    }
}