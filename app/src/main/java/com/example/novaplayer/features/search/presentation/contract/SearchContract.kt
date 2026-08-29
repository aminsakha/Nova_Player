package com.example.novaplayer.features.search.presentation.contract

import com.example.novaplayer.features.home.domain.model.Track

object SearchContract {

    data class UiState(
        val query: String = "",
        val searchResults: List<Track> = emptyList(),
        val recentSearches: List<String> = emptyList(),
        val isLoading: Boolean = true,
        val error: SearchError? = null
    ) {
        val hasNoResults: Boolean
            get() = query.isNotBlank() &&
                    searchResults.isEmpty() &&
                    !isLoading &&
                    error == null
    }

    sealed interface UiAction {

        data class QueryChanged(
            val query: String
        ) : UiAction

        data class RecentSearchSelected(
            val query: String
        ) : UiAction

        data class DeleteRecentSearch(
            val query: String
        ) : UiAction

        data class TrackSelected(
            val track: Track
        ) : UiAction

        data object SubmitSearch : UiAction

        data object Retry : UiAction
    }
}

enum class SearchError {
    PERMISSION_DENIED
}
