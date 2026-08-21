package com.example.novaplayer.features.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.core.media.controller.PlayerController
import com.example.novaplayer.features.player.domain.CurrentSong
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerController: PlayerController
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(PlayerContract.UiState())

    val uiState: StateFlow<PlayerContract.UiState> =
        _uiState.asStateFlow()

    private var isPlayerConnected = false

    private var pendingSong: CurrentSong? = null

    init {
        connectToPlayer()
        observePlaybackErrors()
        observePlaybackProgress()
    }

    fun onAction(
        action: PlayerContract.UiAction
    ) {
        when (action) {

            is PlayerContract.UiAction.SelectSong -> {
                selectSong(action.song)
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
                showQueueUnavailableError()
            }

            PlayerContract.UiAction.Previous -> {
                showQueueUnavailableError()
            }

            PlayerContract.UiAction.ClearError -> {
                clearError()
            }
        }
    }

    private fun connectToPlayer() {
        viewModelScope.launch {

            runCatching {
                playerController.connect()
            }.onSuccess {

                isPlayerConnected = true

                pendingSong?.let { song ->
                    pendingSong = null
                    startSelectedSong(song)
                }

            }.onFailure { error ->

                isPlayerConnected = false

                _uiState.update { currentState ->
                    currentState.copy(
                        errorMessage =
                            error.message
                                ?: "Unable to connect to player"
                    )
                }
            }
        }
    }

    private fun observePlaybackErrors() {
        viewModelScope.launch {

            playerController.playbackErrors.collect { playbackError ->

                _uiState.update { currentState ->
                    currentState.copy(
                        playbackStatus =
                            PlaybackStatus.PAUSED,
                        errorMessage =
                            playbackError.toString()
                    )
                }
            }
        }
    }

    private fun selectSong(
        song: CurrentSong
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                currentSong = song,
                playbackStatus =
                    PlaybackStatus.PAUSED,
                currentPositionMs = 0L,
                durationMs = 0L,
                errorMessage = null
            )
        }

        if (isPlayerConnected) {
            startSelectedSong(song)
        } else {
            pendingSong = song
        }
    }

    private fun startSelectedSong(
        song: CurrentSong
    ) {
        if (!isPlayerConnected) {
            pendingSong = song
            return
        }

        playerController.playSelectedSong(
            uri = song.uri
        )

        _uiState.update { currentState ->
            currentState.copy(
                currentSong = song,
                playbackStatus =
                    PlaybackStatus.PLAYING,
                currentPositionMs = 0L,
                errorMessage = null
            )
        }
    }

    private fun playPause() {

        val currentState =
            _uiState.value

        if (currentState.currentSong == null) {

            _uiState.update {
                it.copy(
                    errorMessage = "No song selected"
                )
            }

            return
        }

        when (currentState.playbackStatus) {

            PlaybackStatus.PLAYING -> {

                playerController.pause()

                _uiState.update {
                    it.copy(
                        playbackStatus =
                            PlaybackStatus.PAUSED
                    )
                }
            }

            PlaybackStatus.PAUSED,
            PlaybackStatus.STOPPED -> {

                playerController.play()

                _uiState.update {
                    it.copy(
                        playbackStatus =
                            PlaybackStatus.PLAYING
                    )
                }
            }
        }
    }

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
                    minimumValue = 0L,
                    maximumValue = duration
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

    private fun observePlaybackProgress() {
        viewModelScope.launch {

            while (isActive) {

                if (isPlayerConnected) {

                    val currentPosition =
                        playerController
                            .getCurrentPosition()

                    val duration =
                        playerController
                            .getDuration()

                    val safePosition =
                        if (duration > 0L) {
                            currentPosition.coerceIn(
                                minimumValue = 0L,
                                maximumValue = duration
                            )
                        } else {
                            currentPosition
                                .coerceAtLeast(0L)
                        }

                    _uiState.update { currentState ->
                        currentState.copy(
                            currentPositionMs =
                                safePosition,
                            durationMs =
                                duration
                        )
                    }
                }

                delay(250L)
            }
        }
    }

    override fun onCleared() {
        playerController.release()
        super.onCleared()
    }
}