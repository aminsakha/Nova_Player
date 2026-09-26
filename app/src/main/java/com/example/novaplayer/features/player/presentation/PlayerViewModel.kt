package com.example.novaplayer.features.player.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.core.datastore.player.TrackStorage
import com.example.novaplayer.core.media.controller.PlayerController
import com.example.novaplayer.features.home.domain.model.Track
import com.example.novaplayer.features.home.domain.usecase.GetTracksUseCase
import com.example.novaplayer.features.player.domain.CurrentSong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerController: PlayerController,
    private val getTrackUseCase: GetTracksUseCase,
    private val trackStorage: TrackStorage
) : ViewModel() {

    private var playlist: List<Track> = emptyList()
    private var currentIndex = -1

    private val _uiState =
        MutableStateFlow(
            PlayerContract.UiState()
        )

    val uiState: StateFlow<PlayerContract.UiState> =
        _uiState.asStateFlow()

    private var isPlayerConnected = false
    private var pendingTrackUri: String? = null

    init {
        observePlaybackErrors()
        observePlaybackProgress()
        loadPlaylist()
        observeCurrentMediaItem()
        observePlayingState()
        connectToPlayer()
    }

    fun onAction(
        action: PlayerContract.UiAction
    ) {
        when (action) {

            is PlayerContract.UiAction.SelectSong -> {
                selectSong(action.trackUri)
            }

            PlayerContract.UiAction.PlayPause -> {
                playPause()
            }

            PlayerContract.UiAction.Stop -> {
                stop()
            }

            is PlayerContract.UiAction.SeekTo -> {
                seekTo(action.positionMs)
            }

            PlayerContract.UiAction.Next -> {
                playerController.next()
            }

            PlayerContract.UiAction.Previous -> {
                playerController.previous()
            }

            PlayerContract.UiAction.ClearError -> {
                clearError()
            }
        }
    }

    private fun loadPlaylist() {
        viewModelScope.launch {

            playlist =
                getTrackUseCase.getAllTrack()

            Log.d(
                "PLAYER_TEST",
                "Playlist size = ${playlist.size}"
            )
        }
    }

    private fun selectSong(
        trackUri: String
    ) {
        if (trackUri.isBlank()) {
            return
        }

        if (!isPlayerConnected) {
            pendingTrackUri = trackUri
            return
        }

        if (playerController.isCurrentMediaItem(trackUri)) {
            Log.d(
                "PLAYER_DEBUG",
                "Same track → skip reload"
            )
            return
        }

        currentIndex =
            playlist.indexOfFirst {
                it.uri == trackUri
            }

        if (currentIndex == -1) {
            Log.d(
                "PLAYER_DEBUG",
                "Track not found in playlist: $trackUri"
            )
            return
        }

        loadTrack(trackUri)
    }

    private fun observePlayingState() {
        viewModelScope.launch {

            playerController.isPlaying.collect { isPlaying ->

                _uiState.update {
                    it.copy(
                        playbackStatus =
                            if (isPlaying) {
                                PlaybackStatus.PLAYING
                            } else {
                                PlaybackStatus.PAUSED
                            }
                    )
                }
            }
        }
    }

    private fun observeCurrentMediaItem() {
        viewModelScope.launch {

            playerController.currentMediaItem.collect { mediaItem ->

                if (mediaItem == null) {
                    return@collect
                }

                val trackUri =
                    mediaItem
                        .localConfiguration
                        ?.uri
                        ?.toString()
                        ?: return@collect

                val track =
                    getTrackUseCase.getTrack(trackUri)
                        ?: return@collect

                val currentSong =
                    track.toCurrentSong()

                _uiState.update {
                    it.copy(
                        currentSong = currentSong,
                        durationMs = track.duration,
                        currentPositionMs = 0L
                    )
                }
            }
        }
    }

    private fun loadTrack(
        uri: String
    ) {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    playbackStatus = PlaybackStatus.PAUSED,
                    currentPositionMs = 0L,
                    durationMs = 0L,
                    errorMessage = null
                )
            }

            try {

                val track =
                    getTrackUseCase.getTrack(uri)

                if (track == null) {

                    _uiState.update {
                        it.copy(
                            currentSong = null,
                            errorMessage = "Track not found"
                        )
                    }

                    return@launch
                }

                startSelectedSong(
                    track.toCurrentSong()
                )

            } catch (exception: Exception) {

                _uiState.update {
                    it.copy(
                        playbackStatus = PlaybackStatus.PAUSED,
                        errorMessage =
                            exception.message
                                ?: "Unable to load track"
                    )
                }
            }
        }
    }

    private fun connectToPlayer() {
        viewModelScope.launch {

            try {

                playerController.connect()

                isPlayerConnected = true

                restoreLastTrack()

                pendingTrackUri?.let { uri ->

                    pendingTrackUri = null

                    currentIndex =
                        playlist.indexOfFirst {
                            it.uri == uri
                        }

                    loadTrack(uri)
                }

            } catch (exception: Exception) {

                isPlayerConnected = false

                _uiState.update {
                    it.copy(
                        errorMessage =
                            exception.message
                                ?: "Unable to connect to player"
                    )
                }
            }
        }
    }

    private fun startSelectedSong(
        song: CurrentSong
    ) {

        if (!isPlayerConnected) {
            pendingTrackUri = song.uri
            return
        }

        val currentSongs =
            playlist.map { track ->
                track.toCurrentSong()
            }

        playerController.playPlaylist(
            songs = currentSongs,
            selectedIndex = currentIndex
        )

        _uiState.update {
            it.copy(
                currentSong = song,
                playbackStatus = PlaybackStatus.PLAYING,
                currentPositionMs = 0L,
                durationMs = song.duration,
                errorMessage = null
            )
        }
    }

    private fun playPause() {
        viewModelScope.launch {

            val currentSong =
                _uiState.value.currentSong

            if (currentSong == null) {

                _uiState.update {
                    it.copy(
                        errorMessage = "No song selected"
                    )
                }

                return@launch
            }

            if (!isPlayerConnected) {

                _uiState.update {
                    it.copy(
                        errorMessage = "Player is not connected"
                    )
                }

                return@launch
            }

            if (playerController.isPlaying()) {
                playerController.pause()
            } else {
                playerController.play()
            }
        }
    }

    /**
     * Restore only the last selected track.
     *
     * DataStore:
     *      last_track_id
     *
     * MediaStore:
     *      Track information
     *
     * Media3:
     *      Current MediaItem
     */
    private suspend fun restoreLastTrack() {

        if (playerController.hasCurrentMediaItem()) {

            Log.d(
                "PLAYER_RESTORE",
                "Media3 already has current track"
            )

            return
        }

        val trackId =
            trackStorage
                .observeCurrentTrackId()
                .firstOrNull()
                ?.toLongOrNull()

        if (trackId == null) {

            Log.d(
                "PLAYER_RESTORE",
                "No saved track ID"
            )

            return
        }

        Log.d(
            "PLAYER_RESTORE",
            "Restoring track ID = $trackId"
        )

        val track =
            getTrackUseCase.getTrackById(
                trackId
            )

        if (track == null) {

            Log.d(
                "PLAYER_RESTORE",
                "Track not found: $trackId"
            )

            return
        }

        val currentSong =
            track.toCurrentSong()

        playerController.restoreSong(
            currentSong
        )

        _uiState.update {
            it.copy(
                currentSong = currentSong,
                playbackStatus = PlaybackStatus.PAUSED,
                currentPositionMs = 0L,
                durationMs = currentSong.duration,
                errorMessage = null
            )
        }

        Log.d(
            "PLAYER_RESTORE",
            "Track restored: ${currentSong.title}"
        )
    }

    private fun stop() {

        if (!isPlayerConnected) {
            return
        }

        playerController.stop()

        _uiState.update {
            it.copy(
                playbackStatus = PlaybackStatus.STOPPED,
                currentPositionMs = 0L
            )
        }
    }

    private fun seekTo(
        positionMs: Long
    ) {

        if (!isPlayerConnected) {
            return
        }

        val duration =
            playerController.getDuration()

        val safePosition =
            if (duration > 0L) {
                positionMs.coerceIn(
                    0L,
                    duration
                )
            } else {
                positionMs.coerceAtLeast(0L)
            }

        playerController.seekTo(
            position = safePosition
        )

        _uiState.update {
            it.copy(
                currentPositionMs = safePosition,
                durationMs = duration
            )
        }
    }

    private fun observePlaybackErrors() {
        viewModelScope.launch {

            playerController.playbackErrors.collect { error ->

                _uiState.update {
                    it.copy(
                        playbackStatus = PlaybackStatus.PAUSED,
                        errorMessage = error.toString()
                    )
                }
            }
        }
    }

    private fun observePlaybackProgress() {
        viewModelScope.launch {

            while (isActive) {

                if (isPlayerConnected) {

                    val currentPosition =
                        playerController.getCurrentPosition()

                    val duration =
                        playerController.getDuration()

                    val safeDuration =
                        duration.coerceAtLeast(0L)

                    val safePosition =
                        if (safeDuration > 0L) {
                            currentPosition.coerceIn(
                                0L,
                                safeDuration
                            )
                        } else {
                            currentPosition.coerceAtLeast(0L)
                        }

                    _uiState.update {
                        it.copy(
                            currentPositionMs = safePosition,
                            durationMs = safeDuration
                        )
                    }
                }

                delay(250L)
            }
        }
    }

    private fun clearError() {
        _uiState.update {
            it.copy(
                errorMessage = null
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

/**
 * Track -> CurrentSong
 */
private fun Track.toCurrentSong(): CurrentSong {
    return CurrentSong(
        id = id,
        uri = uri,
        title = title,
        artist = artist,
        album = album,
        duration = duration,
        albumArtUri = albumArtUri
    )
}