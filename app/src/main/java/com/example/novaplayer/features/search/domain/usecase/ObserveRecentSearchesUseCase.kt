package com.example.novaplayer.features.search.domain.usecase

import com.example.novaplayer.features.search.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRecentSearchesUseCase @Inject constructor(
    private val repository: SearchHistoryRepository
) {

    operator fun invoke(): Flow<List<String>> {
        return repository.observeRecentSearches()
    }
}
