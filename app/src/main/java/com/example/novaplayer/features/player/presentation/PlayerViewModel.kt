package com.example.novaplayer.features.player.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.core.media.controller.PlayerController
import com.example.novaplayer.features.home.domain.model.Track
import com.example.novaplayer.features.home.domain.usecase.GetTracksUseCase
import com.example.novaplayer.features.player.domain.CurrentSong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerController: PlayerController,
    private val getTrackUseCase: GetTracksUseCase
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
        connectToPlayer()
        observePlaybackErrors()
        observePlaybackProgress()
        loadPlaylist()
        observeCurrentMediaItem()
        observePlayingState()
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

            playlist = getTrackUseCase.getAllTrack()

            Log.d(
                "PLAYER_TEST",
                "Playlist size = ${playlist.size}"
            )
        }
    }

    // ------------------------------------------------------------------------
    // Select Song
    // ------------------------------------------------------------------------

    private fun selectSong(
        trackUri: String
    ) {
        currentIndex = playlist.indexOfFirst {
            it.uri == trackUri
        }
        if (trackUri.isBlank()) {
            _uiState.update {
                it.copy(
                    errorMessage = "Invalid track URI"
                )
            }
            return
        }

        if (!isPlayerConnected) {
            pendingTrackUri = trackUri

            _uiState.update {
                it.copy(
                    playbackStatus = PlaybackStatus.PAUSED,
                    currentPositionMs = 0L,
                    durationMs = 0L,
                    errorMessage = null
                )
            }

            return
        }

        if (playerController.isCurrentMediaItem(trackUri)) {
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
                    mediaItem.localConfiguration
                        ?.uri
                        ?.toString()
                        ?: return@collect

                val track =
                    getTrackUseCase.getTrack(trackUri)
                        ?: return@collect

                val currentSong =
                    CurrentSong(
                        id = track.id,
                        uri = track.uri,
                        title = track.title,
                        artist = track.artist,
                        album = track.album,
                        duration = track.duration,
                        albumArtUri = track.albumArtUri
                    )

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

    // ------------------------------------------------------------------------
    // Load Track
    // ------------------------------------------------------------------------

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

                /*
                 * فقط URI را به UseCase می‌دهیم.
                 *
                 * UseCase مسئول پیدا کردن Track از Repository است.
                 */
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

                /*
                 * تبدیل مدل Home به مدل مخصوص Player
                 */
                val currentSong =
                    CurrentSong(
                        id = track.id,
                        uri = track.uri,
                        title = track.title,
                        artist = track.artist,
                        album = track.album,
                        duration = track.duration,
                        albumArtUri = track.albumArtUri
                    )

                startSelectedSong(currentSong)

            } catch (exception: Exception) {

                _uiState.update {
                    it.copy(
                        playbackStatus =
                            PlaybackStatus.PAUSED,
                        errorMessage =
                            exception.message
                                ?: "Unable to load track"
                    )
                }
            }
        }
    }

    // ------------------------------------------------------------------------
    // Player Connection
    // ------------------------------------------------------------------------

    private fun connectToPlayer() {
        viewModelScope.launch {

            try {

                playerController.connect()

                isPlayerConnected = true

                pendingTrackUri?.let { uri ->

                    pendingTrackUri = null

                    loadTrack(uri)
                    Log.d(
                        "PLAYER_TEST",currentIndex.toString())
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

    // ------------------------------------------------------------------------
    // Start Song
    // ------------------------------------------------------------------------

    private fun startSelectedSong(
        song: CurrentSong
    ) {
        if (!isPlayerConnected) {
            pendingTrackUri = song.uri
            return
        }

        val currentSongs = playlist.map { track ->
            CurrentSong(
                id = track.id,
                uri = track.uri,
                title = track.title,
                artist = track.artist,
                album = track.album,
                duration = track.duration,
                albumArtUri = track.albumArtUri
            )
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

    // ------------------------------------------------------------------------
    // Play / Pause
    // ------------------------------------------------------------------------

    private fun playPause() {

        val currentSong =
            _uiState.value.currentSong

        if (currentSong == null) {
            _uiState.update {
                it.copy(
                    errorMessage = "No song selected"
                )
            }
            return
        }

        if (!isPlayerConnected) {
            _uiState.update {
                it.copy(
                    errorMessage = "Player is not connected"
                )
            }
            return
        }

        if (playerController.isPlaying()) {
            playerController.pause()
        } else {
            playerController.play()
        }
    }

    // ------------------------------------------------------------------------
    // Stop
    // ------------------------------------------------------------------------

    private fun stop() {

        if (!isPlayerConnected) {
            return
        }

        playerController.stop()

        _uiState.update {
            it.copy(
                playbackStatus =
                    PlaybackStatus.STOPPED,
                currentPositionMs = 0L
            )
        }
    }

    // ------------------------------------------------------------------------
    // Seek
    // ------------------------------------------------------------------------

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
                currentPositionMs = safePosition
            )
        }
    }

    // ------------------------------------------------------------------------
    // Playback Errors
    // ------------------------------------------------------------------------

    private fun observePlaybackErrors() {
        viewModelScope.launch {

            playerController.playbackErrors.collect { error ->

                _uiState.update {
                    it.copy(
                        playbackStatus =
                            PlaybackStatus.PAUSED,
                        errorMessage =
                            error.toString()
                    )
                }
            }
        }
    }

    // ------------------------------------------------------------------------
    // Playback Progress
    // ------------------------------------------------------------------------

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
                            currentPositionMs =
                                safePosition,
                            durationMs =
                                safeDuration
                        )
                    }
                }

                delay(250L)
            }
        }
    }

    // ------------------------------------------------------------------------
    // Error
    // ------------------------------------------------------------------------

    private fun clearError() {
        _uiState.update {
            it.copy(
                errorMessage = null
            )
        }
    }

    private fun showQueueUnavailableError() {
        _uiState.update {
            it.copy(
                errorMessage =
                    "Playback queue is not available yet"
            )
        }
    }

    // ------------------------------------------------------------------------
    // Cleanup
    // ------------------------------------------------------------------------

    override fun onCleared() {

        super.onCleared()
    }
}