package com.example.novaplayer.features.settings.domain.usecase

import com.example.novaplayer.features.settings.domain.model.ThemeMode
import com.example.novaplayer.features.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val repository: SettingsRepository
){
    suspend operator fun invoke(themeMode: ThemeMode){
        repository.setTheme(themeMode)
    }
}