package com.example.novaplayer.features.search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.features.home.domain.model.Track
import com.example.novaplayer.features.home.domain.usecase.GetTracksUseCase
import com.example.novaplayer.features.search.domain.usecase.DeleteRecentSearchUseCase
import com.example.novaplayer.features.search.domain.usecase.ObserveRecentSearchesUseCase
import com.example.novaplayer.features.search.domain.usecase.SaveRecentSearchUseCase
import com.example.novaplayer.features.search.domain.usecase.SearchTracksUseCase
import com.example.novaplayer.features.search.presentation.contract.SearchContract
import com.example.novaplayer.features.search.presentation.contract.SearchError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getTracksUseCase: GetTracksUseCase,
    private val searchTracksUseCase: SearchTracksUseCase,
    private val observeRecentSearchesUseCase:
    ObserveRecentSearchesUseCase,
    private val saveRecentSearchUseCase:
    SaveRecentSearchUseCase,
    private val deleteRecentSearchUseCase:
    DeleteRecentSearchUseCase
) : ViewModel() {

    private var allTracks: List<Track> = emptyList()

    private val _uiState = MutableStateFlow(
        SearchContract.UiState()
    )

    val uiState: StateFlow<SearchContract.UiState> =
        _uiState.asStateFlow()

    init {
        observeRecentSearches()
        loadTracks()
    }

    fun onAction(action: SearchContract.UiAction) {
        when (action) {
            is SearchContract.UiAction.QueryChanged -> {
                updateQuery(action.query)
            }

            is SearchContract.UiAction.RecentSearchSelected -> {
                updateQuery(action.query)
            }

            is SearchContract.UiAction.DeleteRecentSearch -> {
                deleteRecentSearch(action.query)
            }

            is SearchContract.UiAction.TrackSelected -> {
                saveCurrentQuery()
            }

            SearchContract.UiAction.SubmitSearch -> {
                saveCurrentQuery()
            }

            SearchContract.UiAction.Retry -> {
                loadTracks()
            }
        }
    }

    private fun loadTracks() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                allTracks =
                    getTracksUseCase.getAllTrack()

                _uiState.update { currentState ->
                    currentState.copy(
                        searchResults = searchTracksUseCase(
                            tracks = allTracks,
                            query = currentState.query
                        ),
                        isLoading = false,
                        error = null
                    )
                }
            } catch (error: SecurityException) {
                Log.e(
                    TAG,
                    "Audio permission is not available",
                    error
                )

                _uiState.update { currentState ->
                    currentState.copy(
                        searchResults = emptyList(),
                        isLoading = false,
                        error = SearchError.PERMISSION_DENIED
                    )
                }
            }
        }
    }

    private fun updateQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                query = query,
                searchResults = searchTracksUseCase(
                    tracks = allTracks,
                    query = query
                )
            )
        }
    }

    private fun observeRecentSearches() {
        viewModelScope.launch {
            observeRecentSearchesUseCase()
                .collect { recentSearches ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            recentSearches = recentSearches
                        )
                    }
                }
        }
    }

    private fun saveCurrentQuery() {
        val query = _uiState.value.query.trim()

        if (query.isEmpty()) {
            return
        }

        viewModelScope.launch {
            saveRecentSearchUseCase(query)
        }
    }

    private fun deleteRecentSearch(query: String) {
        viewModelScope.launch {
            deleteRecentSearchUseCase(query)
        }
    }

    private companion object {
        const val TAG = "SearchViewModel"
    }
}