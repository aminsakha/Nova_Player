package com.example.novaplayer.features.playlist.presentation

sealed interface PlaylistIntent {

    data object AddPlaylistClicked : PlaylistIntent

    data object DismissCreatePlaylist : PlaylistIntent

    data class PlaylistNameChanged(
        val name: String
    ) : PlaylistIntent

    data class PlaylistCoverChanged(
        val cover: String?
    ) : PlaylistIntent

    data object CreatePlaylistClicked : PlaylistIntent

    data class DeletePlaylist(
        val playlist: PlaylistItemUi
    ) : PlaylistIntent
}