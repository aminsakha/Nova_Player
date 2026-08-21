package com.example.novaplayer.features.settings.domain.usecase

import com.example.novaplayer.features.settings.domain.model.ThemeMode
import com.example.novaplayer.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveThemeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<ThemeMode>{
        return repository.observeTheme()
    }
}