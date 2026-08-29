package com.example.novaplayer.features.search.data.di

import com.example.novaplayer.features.search.data.repository.SearchHistoryRepositoryImpl
import com.example.novaplayer.features.search.domain.repository.SearchHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchModule {

    @Binds
    @Singleton
    abstract fun bindSearchHistoryRepository(
        repositoryImpl: SearchHistoryRepositoryImpl
    ): SearchHistoryRepository
}