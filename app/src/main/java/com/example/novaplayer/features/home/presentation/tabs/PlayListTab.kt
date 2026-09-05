package com.example.novaplayer.features.home.presentation.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.novaplayer.features.playlist.presentation.CreatePlaylistCard
import com.example.novaplayer.features.playlist.presentation.PlaylistIntent
import com.example.novaplayer.features.playlist.presentation.PlaylistItem
import com.example.novaplayer.features.playlist.presentation.PlaylistState


@Composable
fun PlayListTab(
    state: PlaylistState,
    onIntent: (PlaylistIntent) -> Unit,
    onPlaylistClick: (Long) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            items(
                items = state.playlists,
                key = { it.id }
            ) { playlist ->

                PlaylistItem(
                    playlist = playlist,

                    onClick = {
                        onPlaylistClick(playlist.id)
                    },

                    onDelete = {
                        onIntent(
                            PlaylistIntent.DeletePlaylist(
                                playlist
                            )
                        )
                    }
                )
            }
        }

        if (state.showCreatePlaylist) {

            CreatePlaylistCard(
                name = state.playlistName,

                onNameChange = {
                    onIntent(
                        PlaylistIntent.PlaylistNameChanged(it)
                    )
                },

                onCreateClick = {
                    onIntent(
                        PlaylistIntent.CreatePlaylistClicked
                    )
                },

                onDismiss = {
                    onIntent(
                        PlaylistIntent.DismissCreatePlaylist
                    )
                },
                cover = "TODO()",
                onCoverChange = { TODO() }
            )
        }
    }
}
