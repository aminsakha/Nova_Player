package com.example.novaplayer.core.media.model

sealed interface PlaybackError {

    data object EmptySongUri : PlaybackError

    data object PlayerNotConnected : PlaybackError

    data object InvalidSongUri : PlaybackError

    data object PermissionDenied : PlaybackError

    data object InvalidPlayerState : PlaybackError

    data class PlaybackFailed(
        val errorCode: Int
    ) : PlaybackError
}