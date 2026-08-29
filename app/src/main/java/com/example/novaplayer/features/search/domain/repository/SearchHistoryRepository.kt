package com.example.novaplayer.features.search.domain.repository

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    fun observeRecentSearches(): Flow<List<String>>

    suspend fun saveSearch(query: String)

    suspend fun deleteSearch(query: String)
}