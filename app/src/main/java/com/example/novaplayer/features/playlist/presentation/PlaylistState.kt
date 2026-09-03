package com.example.novaplayer.features.playlist.presentation


data class PlaylistState(
    val playlists: List<PlaylistItemUi> = emptyList(),

    val showCreatePlaylist: Boolean = false,

    val playlistName: String = "",

    val playlistCover: String? = null,

    val isLoading: Boolean = false,

    val error: String? = null,
    val selectedPlaylistId: Long? = null

)