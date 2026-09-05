package com.example.novaplayer.features.playlist.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.novaplayer.core.navigation.PlaylistDetailRoute
import com.example.novaplayer.features.home.domain.model.Track
import com.example.novaplayer.features.home.domain.usecase.GetTracksUseCase
import com.example.novaplayer.features.playlist.domain.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlaylistDetailState(
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    private val getTracksUseCase: GetTracksUseCase,
    private val playlistRepository: PlaylistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<PlaylistDetailRoute>()
    private val playlistId = route.playlistId

    private val _state = MutableStateFlow(PlaylistDetailState())
    val state = _state.asStateFlow()

    init {
        loadPlaylistTracks()
    }

    private fun loadPlaylistTracks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            playlistRepository.getTracksForPlaylist(playlistId).collect { trackIds ->
                val allTracks = getTracksUseCase.getAllTrack()
                val playlistTracks = allTracks.filter { trackIds.contains(it.id) }
                _state.update { it.copy(tracks = playlistTracks, isLoading = false) }
            }
        }
    }
}
