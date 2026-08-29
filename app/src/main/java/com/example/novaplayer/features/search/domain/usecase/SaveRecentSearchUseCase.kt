package com.example.novaplayer.features.search.domain.usecase

import com.example.novaplayer.features.search.domain.repository.SearchHistoryRepository
import javax.inject.Inject

class SaveRecentSearchUseCase @Inject constructor(
    private val repository: SearchHistoryRepository
) {

    suspend operator fun invoke(query: String) {
        repository.saveSearch(query)
    }
}