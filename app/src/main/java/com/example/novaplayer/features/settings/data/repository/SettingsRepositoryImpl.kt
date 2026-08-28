package com.example.novaplayer.features.settings.data.repository

import com.example.novaplayer.core.datastore.PreferenceStorage
import com.example.novaplayer.features.settings.domain.model.AppLanguage
import com.example.novaplayer.features.settings.domain.model.ThemeMode
import com.example.novaplayer.features.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : SettingsRepository {

    private companion object {
        const val THEME_KEY = "Theme"
        const val DEFAULT_THEME = "SYSTEM"

        const val LANGUAGE_KEY = "Language"
        const val DEFAULT_LANGUAGE = "EN"
    }

    override fun observeTheme(): Flow<ThemeMode> {
        return preferenceStorage
            .observeString(
                key = THEME_KEY,
                defaultValue = DEFAULT_THEME


            )
            .map { theme ->
                ThemeMode.valueOf(theme)
            }
    }

    override suspend fun setTheme(themeMode: ThemeMode) {
        preferenceStorage.setString(
            key = THEME_KEY,
            value = themeMode.name
        )
    }

    override fun observeLanguage(): Flow<AppLanguage> {
        return preferenceStorage
            .observeString(
                key = LANGUAGE_KEY,
                defaultValue = DEFAULT_LANGUAGE
            )
            .map { language ->
                AppLanguage.valueOf(language)
            }
    }

    override suspend fun setLanguage(language: AppLanguage) {
        preferenceStorage.setString(
            key = LANGUAGE_KEY,
            value = language.name
        )
    }
}