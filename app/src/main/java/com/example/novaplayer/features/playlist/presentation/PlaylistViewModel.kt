package com.example.novaplayer.features.playlist.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.features.playlist.data.local.entity.playListEntity
import com.example.novaplayer.features.playlist.domain.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val repository: PlaylistRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PlaylistState())

    val state: StateFlow<PlaylistState> = _state.asStateFlow()

    init {
        observePlaylists()
    }

    fun onIntent(intent: PlaylistIntent) {

        when (intent) {


            PlaylistIntent.AddPlaylistClicked -> {
                Log.d("PlaylistVM", "ADD CLICKED")

                _state.update {
                    it.copy(
                        showCreatePlaylist = true
                    )
                }
            }

            PlaylistIntent.DismissCreatePlaylist -> {
                _state.update {
                    it.copy(
                        showCreatePlaylist = false,
                        playlistName = "",
                        playlistCover = null
                    )
                }
            }

            is PlaylistIntent.PlaylistNameChanged -> {
                _state.update {
                    it.copy(
                        playlistName = intent.name
                    )
                }
            }

            is PlaylistIntent.PlaylistCoverChanged -> {
                _state.update {
                    it.copy(
                        playlistCover = intent.cover
                    )
                }
            }

            PlaylistIntent.CreatePlaylistClicked -> {
                createPlaylist()
            }

            is PlaylistIntent.DeletePlaylist -> {
                deletePlaylist(intent.playlist)
            }
        }
    }

    private fun observePlaylists() {

        viewModelScope.launch {

            repository
                .getPlaylistsWithCount()
                .collect { playlists ->

                    _state.update {
                        it.copy(
                            playlists = playlists.map { playlist ->

                                PlaylistItemUi(
                                    id = playlist.id,
                                    title = playlist.name,
                                    cover = playlist.imageUri,
                                    songCount = playlist.trackCount
                                )
                            }
                        )
                    }
                }
        }
    }

    private fun createPlaylist() {

        val name = state.value.playlistName.trim()

        if (name.isEmpty()) {
            return
        }

        viewModelScope.launch {

            repository.addPlaylist(name)

            _state.update {
                it.copy(
                    showCreatePlaylist = false,
                    playlistName = "",
                    playlistCover = null
                )
            }
        }
    }

    private fun deletePlaylist(
        playlist: PlaylistItemUi
    ) {

        viewModelScope.launch {

            repository.deletePlaylist(
                playListEntity(
                    id = playlist.id,
                    name = playlist.title,
                    imageUri = playlist.cover
                )
            )
        }
    }
}