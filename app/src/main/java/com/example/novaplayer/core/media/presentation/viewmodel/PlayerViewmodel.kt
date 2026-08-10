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
    private val _uiState = MutableStateFlow(Media3Contract.UiState())
    val uiState: StateFlow<Media3Contract.UiState> = _uiState.asStateFlow()

    fun onAction(action: Media3Contract.UiAction) {
        when (action) {
            is Media3Contract.UiAction.play -> play()

            is Media3Contract.UiAction.pause -> pause()

            is Media3Contract.UiAction.stop -> stop()

            is Media3Contract.UiAction.seekTo -> {
                seekTo(action.position)
            }

            is Media3Contract.UiAction.playLocal -> {
                playLocal()
            }

            is Media3Contract.UiAction.PlaySelectedSong -> {
                playSelectedSong(action.uri)
            }

            is Media3Contract.UiAction.ClearError -> {
                clearError()
            }
        }
    }


    //first operation
    init {
        viewModelScope.launch {
            playerController.connect()

            Log.d(
                "MEDIA3",
                "controller connected..."
            )
        }

        viewModelScope.launch {
            playerController.playbackError.collect { errorMessage ->
                _uiState.update { currentState ->
                    currentState.copy(
                        playState =
                            if (errorMessage != null) {
                                PlayState.pause
                            } else {
                                currentState.playState
                            },
                        errorMessage = errorMessage
                    )
                }
            }
        }
    }

    private fun clearError() {
        playerController.clearPlaybackError()
    }

    private fun play() {
        viewModelScope.launch {
            playerController.play()
            _uiState.update { it.copy(playState = PlayState.playing) }
        }

    }

    private fun pause() {
        viewModelScope.launch {
            _uiState.update { it.copy(playState = PlayState.pause) }
            playerController.pause()

        }

    }

    private fun stop() {
        playerController.stop()

        _uiState.update {
            it.copy(
                playState = PlayState.stop
            )
        }
    }

    private fun seekTo(position: Long) {
        playerController.seekTo(position)

    }


    override fun onCleared() {
        playerController.release()
        super.onCleared()
    }

    private fun playSelectedSong(uri: String) {
        playerController.playSelectedSong(uri)

        _uiState.update {
            it.copy(
                playState = PlayState.playing,
                selectedSongUri = uri
            )
        }
    }

    private fun playLocal() {
        viewModelScope.launch {
            playerController.playRaw(R.raw.vinak)
            _uiState.update { it.copy(playState = PlayState.playing) }
        }


    }

}