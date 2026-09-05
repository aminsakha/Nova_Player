package com.example.novaplayer.features.settings.domain.repository

import com.example.novaplayer.features.settings.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository{
    fun observeTheme(): Flow<ThemeMode>

    suspend fun setTheme(themeMode: ThemeMode)
}