package com.example.novaplayer.features.playlist.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PlaylistScreen(
    onPlaylistClick: (Long) -> Unit,
    viewModel: PlaylistViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LazyColumn {

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
                    viewModel.onIntent(
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
                viewModel.onIntent(
                    PlaylistIntent.PlaylistNameChanged(it)
                )
            },

            onCreateClick = {
                viewModel.onIntent(
                    PlaylistIntent.CreatePlaylistClicked
                )
            },

            onDismiss = {
                viewModel.onIntent(
                    PlaylistIntent.DismissCreatePlaylist
                )
            },

            cover = "",
            onCoverChange = { TODO() }
        )
    }
}