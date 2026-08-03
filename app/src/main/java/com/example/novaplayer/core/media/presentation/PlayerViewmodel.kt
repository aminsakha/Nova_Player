package com.example.novaplayer.core.media.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.core.media.controller.PlayerController
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerController: PlayerController
) : ViewModel() {
    //first operation
    init {
        viewModelScope.launch {
            playerController.connect()
        }
    }

    fun play() {
        playerController.play()
    }

    fun pause() {
        playerController.pause()
    }

    fun seekTo(position: Long) {
        playerController.seekTo(position)
    }

    override fun onCleared() {
        playerController.release()
        super.onCleared()
    }
}