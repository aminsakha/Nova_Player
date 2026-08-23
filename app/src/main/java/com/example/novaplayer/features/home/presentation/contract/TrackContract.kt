package com.example.novaplayer.features.home.presentation.contract

import com.example.novaplayer.features.home.domain.model.Track

object TrackContract {

    data class UiState(
        val tracks: List<Track> = emptyList(),
        val loadingState: LoadingState = LoadingState.IDLE
    )

    sealed interface UiAction {
        data object GetTracks : UiAction
    }
}

enum class LoadingState {
    IDLE,
    LOADING,
    SUCCESS,
    ERROR
}