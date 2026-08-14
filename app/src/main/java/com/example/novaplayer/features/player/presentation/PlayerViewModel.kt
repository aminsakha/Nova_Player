package com.example.novaplayer.features.player.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.core.media.controller.PlayerController
import com.example.novaplayer.features.player.domain.CurrentSong
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
            playerController.playbackError.collect {
                    errorMessage ->

                _uiState.update { currentState ->
                    currentState.copy(
                        playbackStatus =
                            if (errorMessage != null) {
                                PlaybackStatus.PAUSED
                            } else {
                                currentState.playbackStatus
                            },
                        errorMessage = errorMessage
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
        val currentState = _uiState.value

        if (currentState.currentSong == null) {
            _uiState.update {
                it.copy(
                    errorMessage = "No song selected"
                )
            }
            return
        }

        if (
            currentState.playbackStatus ==
            PlaybackStatus.PLAYING
        ) {
            playerController.pause()

            _uiState.update {
                it.copy(
                    playbackStatus =
                        PlaybackStatus.PAUSED
                )
            }
        } else {
            playerController.play()

            _uiState.update {
                it.copy(
                    playbackStatus =
                        PlaybackStatus.PLAYING
                )
            }
        }
    }

    private fun stop() {
        playerController.stop()

        _uiState.update {
            it.copy(
                playbackStatus =
                    PlaybackStatus.STOPPED
            )
        }
    }

    private fun seekTo(
        positionMs: Long
    ) {
        val safePosition = positionMs.coerceAtLeast(0L)

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
        playerController.clearPlaybackError()

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

    override fun onCleared() {
        playerController.release()
        super.onCleared()
    }
}

