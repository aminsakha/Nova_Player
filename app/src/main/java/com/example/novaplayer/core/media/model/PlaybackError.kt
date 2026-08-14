package com.example.novaplayer.core.media.model

sealed interface PlaybackError {

    data object EmptySongUri : PlaybackError

    data object PlayerNotConnected : PlaybackError

    data class PlaybackFailed(
        val errorCode: Int
    ) : PlaybackError

    data object UnableToPlaySelectedSong : PlaybackError
}
