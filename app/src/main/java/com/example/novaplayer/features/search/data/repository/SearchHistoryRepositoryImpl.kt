package com.example.novaplayer.features.search.data.repository

import android.util.Log
import com.example.novaplayer.core.datastore.PreferenceStorage
import com.example.novaplayer.features.search.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : SearchHistoryRepository {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun observeRecentSearches(): Flow<List<String>> {
        return preferenceStorage
            .observeString(
                key = RECENT_SEARCHES_KEY,
                defaultValue = EMPTY_SEARCH_HISTORY
            )
            .map { storedValue ->
                decodeRecentSearches(storedValue)
            }
    }

    override suspend fun saveSearch(query: String) {
        val normalizedQuery = query.trim()

        if (normalizedQuery.isEmpty()) {
            return
        }

        val currentSearches =
            observeRecentSearches().first()

        val updatedSearches = buildList {
            add(normalizedQuery)

            addAll(
                currentSearches.filterNot { savedQuery ->
                    savedQuery.equals(
                        other = normalizedQuery,
                        ignoreCase = true
                    )
                }
            )
        }.take(MAX_RECENT_SEARCHES)

        storeRecentSearches(updatedSearches)
    }

    override suspend fun deleteSearch(query: String) {
        val currentSearches =
            observeRecentSearches().first()

        val updatedSearches =
            currentSearches.filterNot { savedQuery ->
                savedQuery == query
            }

        storeRecentSearches(updatedSearches)
    }

    private suspend fun storeRecentSearches(
        recentSearches: List<String>
    ) {
        preferenceStorage.setString(
            key = RECENT_SEARCHES_KEY,
            value = json.encodeToString(
                recentSearches.take(MAX_RECENT_SEARCHES)
            )
        )
    }

    private fun decodeRecentSearches(
        storedValue: String
    ): List<String> {
        return try {
            json.decodeFromString<List<String>>(
                storedValue
            )
                .filter { query ->
                    query.isNotBlank()
                }
                .distinctBy { query ->
                    query.lowercase()
                }
                .take(MAX_RECENT_SEARCHES)
        } catch (error: SerializationException) {
            Log.e(
                TAG,
                "Could not read recent searches",
                error
            )

            emptyList()
        }
    }

    private companion object {
        const val TAG = "SearchHistory"

        const val RECENT_SEARCHES_KEY =
            "recent_searches"

        const val EMPTY_SEARCH_HISTORY =
            "[]"

        const val MAX_RECENT_SEARCHES =
            3
    }
}