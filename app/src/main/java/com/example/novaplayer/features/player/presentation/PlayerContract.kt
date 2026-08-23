package com.example.novaplayer.features.player.presentation

import com.example.novaplayer.features.player.domain.CurrentSong

object PlayerContract {

    data class UiState(
        val currentSong: CurrentSong? = null,
        val playbackStatus: PlaybackStatus = PlaybackStatus.PAUSED,
        val currentPositionMs: Long = 0L,
        val durationMs: Long = 0L,
        val errorMessage: String? = null
    )

    sealed interface UiAction {

        /**
         * Player فقط URI را دریافت می‌کند.
         * ViewModel با استفاده از GetTrackUseCase
         * اطلاعات کامل Track را پیدا می‌کند.
         */
        data class SelectSong(
            val trackUri: String
        ) : UiAction

        data object PlayPause : UiAction

        data object Next : UiAction

        data object Previous : UiAction

        data class SeekTo(
            val positionMs: Long
        ) : UiAction

        data object Stop : UiAction

        data object ClearError : UiAction
    }
}

enum class PlaybackStatus {
    PLAYING,
    PAUSED,
    STOPPED
}