package com.example.novaplayer.features.playlist.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.novaplayer.core.navigation.SelectTrackRoute
import com.example.novaplayer.features.home.domain.model.Track
import com.example.novaplayer.features.home.domain.usecase.GetTracksUseCase
import com.example.novaplayer.features.playlist.domain.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SelectTrackState(
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTrackIds: Set<Long> = emptySet()
)

@HiltViewModel
class SelectTrackViewModel @Inject constructor(
    private val getTracksUseCase: GetTracksUseCase,
    private val playlistRepository: PlaylistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val route = savedStateHandle.toRoute<SelectTrackRoute>()
    private val playlistId = route.playlistId

    private val _state = MutableStateFlow(SelectTrackState())
    val state = _state.asStateFlow()

    init {
        loadTracks()
    }

    private fun loadTracks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val tracks = getTracksUseCase.getAllTrack()
                _state.update { it.copy(tracks = tracks, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onTrackToggle(trackId: Long) {
        _state.update {
            val current = it.selectedTrackIds
            val new = if (current.contains(trackId)) {
                current - trackId
            } else {
                current + trackId
            }
            it.copy(selectedTrackIds = new)
        }
    }

    fun onAddTracksClicked(onComplete: () -> Unit) {
        viewModelScope.launch {
            state.value.selectedTrackIds.forEach { trackId ->
                val track = state.value.tracks.find { it.id == trackId }
                playlistRepository.addTrackToPlaylist(playlistId, trackId, track?.albumArtUri)
            }
            onComplete()
        }
    }
}
