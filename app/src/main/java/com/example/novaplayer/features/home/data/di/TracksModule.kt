package com.example.novaplayer.features.home.data.di

import com.example.novaplayer.features.home.data.datasource.LocalAudioDataSource
import com.example.novaplayer.features.home.data.datasource.MediaStoreAudioDataSource
import com.example.novaplayer.features.home.data.repository.TrackRepositoryImpl
import com.example.novaplayer.features.home.domain.repository.TrackRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TrackDataModule {

    @Binds
    abstract fun bindLocalAudioDataSource(
        implementation: MediaStoreAudioDataSource
    ): LocalAudioDataSource

    @Binds
    abstract fun bindTrackRepository(
        implementation: TrackRepositoryImpl
    ): TrackRepository
}