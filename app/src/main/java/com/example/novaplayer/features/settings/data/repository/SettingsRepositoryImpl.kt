package com.example.novaplayer.features.settings.data.repository

import com.example.novaplayer.core.datastore.PreferenceStorage
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
}