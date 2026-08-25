package com.example.novaplayer.features.miniplayer.presentation

import android.net.Uri
import androidx.media3.common.MediaItem

data class MiniPlayerUiState(
    val currentMediaItem: MediaItem? = null,
    val title: String = "",
    val artist: String = "",
    val artwork: String? = "",
    val isPlaying: Boolean = false
)