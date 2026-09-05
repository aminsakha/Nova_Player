package com.example.novaplayer.features.settings.data.di

import com.example.novaplayer.features.settings.data.repository.SettingsRepositoryImpl
import com.example.novaplayer.features.settings.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsMModule{

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        repositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
}