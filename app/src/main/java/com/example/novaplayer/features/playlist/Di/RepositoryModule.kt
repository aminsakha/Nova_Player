package com.example.novaplayer.features.playlist.Di

import com.example.novaplayer.features.playlist.domain.repository.PlaylistRepository
import com.example.novaplayer.features.playlist.domain.repository.PlaylistRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPlaylistRepository(
        implementation: PlaylistRepositoryImpl
    ): PlaylistRepository
}