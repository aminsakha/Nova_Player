package com.example.novaplayer.core.media.presentation.contract

object Media3Contract {
    data class UiState(
        val playState: PlayState = PlayState.pause
    )

    sealed interface UiAction {
        data object play : UiAction
        data object pause : UiAction
        data class seekTo(val position: Long) : UiAction
        data object playLocal : UiAction
        data class PlaySelectedSong(
            val uri: String
        ) : UiAction
    }
}

enum class PlayState {
    playing,
    pause,
    stop
}