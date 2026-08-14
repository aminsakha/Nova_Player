package com.example.novaplayer.core.media.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.R
import com.example.novaplayer.core.media.controller.PlayerController
import com.example.novaplayer.core.media.presentation.contract.Media3Contract
import com.example.novaplayer.core.media.presentation.contract.PlayState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
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
        MutableStateFlow(Media3Contract.UiState())

    val uiState: StateFlow<Media3Contract.UiState> =
        _uiState.asStateFlow()

    init {
        connectToPlayer()
        observePlaybackErrors()
    }

    fun onAction(
        action: Media3Contract.UiAction
    ) {
        when (action) {
            Media3Contract.UiAction.play -> {
                play()
            }

            Media3Contract.UiAction.pause -> {
                pause()
            }

            Media3Contract.UiAction.stop -> {
                stop()
            }

            is Media3Contract.UiAction.seekTo -> {
                seekTo(action.position)
            }

            Media3Contract.UiAction.playLocal -> {
                playLocal()
            }

            is Media3Contract.UiAction.PlaySelectedSong -> {
                playSelectedSong(action.uri)
            }

            Media3Contract.UiAction.ClearError -> {
                clearError()
            }
        }
    }

    private fun connectToPlayer() {
        viewModelScope.launch {
            playerController.connect()

            Log.d(
                TAG,
                "Controller connected"
            )
        }
    }

    private fun observePlaybackErrors() {
        viewModelScope.launch {
            playerController.playbackErrors.collect {
                    playbackError ->

                _uiState.update { currentState ->
                    currentState.copy(
                        playState = PlayState.pause,
                        selectedSongUri = null,
                        playbackError = playbackError
                    )
                }
            }
        }
    }

    private fun clearError() {
        _uiState.update { currentState ->
            currentState.copy(
                playbackError = null
            )
        }
    }

    private fun play() {
        playerController.play()

        _uiState.update { currentState ->
            currentState.copy(
                playState = PlayState.playing,
                playbackError = null
            )
        }
    }

    private fun pause() {
        playerController.pause()

        _uiState.update { currentState ->
            currentState.copy(
                playState = PlayState.pause
            )
        }
    }

    private fun stop() {
        playerController.stop()

        _uiState.update { currentState ->
            currentState.copy(
                playState = PlayState.stop
            )
        }
    }

    private fun seekTo(position: Long) {
        playerController.seekTo(position)
    }

    private fun playSelectedSong(uri: String) {
        playerController.playSelectedSong(uri)

        _uiState.update { currentState ->
            currentState.copy(
                playState = PlayState.playing,
                selectedSongUri = uri,
                playbackError = null
            )
        }
    }

    private fun playLocal() {
        playerController.playRaw(R.raw.vinak)

        _uiState.update { currentState ->
            currentState.copy(
                playState = PlayState.playing,
                playbackError = null
            )
        }
    }

    override fun onCleared() {
        playerController.release()
        super.onCleared()
    }

    private companion object {
        const val TAG = "MEDIA3"
    }
}