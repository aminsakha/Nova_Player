package com.example.novaplayer.features.settings.domain.usecase

import com.example.novaplayer.features.settings.domain.model.AppLanguage
import com.example.novaplayer.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<AppLanguage> {
        return settingsRepository.observeLanguage()
    }
}