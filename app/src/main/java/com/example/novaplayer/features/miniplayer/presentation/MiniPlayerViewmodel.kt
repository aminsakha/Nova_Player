package com.example.novaplayer.features.miniplayer.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.core.media.controller.PlayerController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MiniPlayerViewmodel @Inject constructor(
    private val mediaController: PlayerController
): ViewModel() {

    val uiState: StateFlow<MiniPlayerUiState> =
        combine(
            mediaController.currentMediaItem,
            mediaController.isPlaying
        ) { mediaItem, isPlaying ->

            MiniPlayerUiState(
                currentMediaItem = mediaItem,
                title = mediaItem
                    ?.mediaMetadata
                    ?.title
                    ?.toString()
                    .orEmpty(),

                artist = mediaItem
                    ?.mediaMetadata
                    ?.artist
                    ?.toString()
                    .orEmpty(),

                artwork = mediaItem
                    ?.mediaMetadata
                    ?.artworkUri
                    ?.toString(),

                isPlaying = isPlaying
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MiniPlayerUiState()
        )
    init {
        viewModelScope.launch {
            uiState.collect { state ->
                Log.d(
                    "MINI_PLAYER",
                    """
                UI STATE
                title = ${state.title}
                artist = ${state.artist}
                artwork = ${state.artwork}
                isPlaying = ${state.isPlaying}
                """.trimIndent()
                )
            }
        }
    }


    fun playPause() {

        if (mediaController.isPlaying()) {
            mediaController.pause()
        } else {
            mediaController.play()
        }
    }
    fun connect() {
        viewModelScope.launch {
            mediaController.connect()
        }
    }

    fun next() {
        mediaController.next()
    }

    fun previous() {
        mediaController.previous()
    }


}