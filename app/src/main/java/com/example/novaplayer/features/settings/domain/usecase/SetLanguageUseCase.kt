package com.example.novaplayer.features.settings.domain.usecase

import com.example.novaplayer.features.settings.domain.model.AppLanguage
import com.example.novaplayer.features.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(language: AppLanguage) {
        settingsRepository.setLanguage(language)
    }
}