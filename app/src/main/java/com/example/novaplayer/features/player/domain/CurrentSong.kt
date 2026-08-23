package com.example.novaplayer.features.player.domain

import com.example.novaplayer.features.home.domain.model.Track

data class CurrentSong(
    val id: Long = 0,
    val uri: String = "",
    val title: String = "",
    val artist: String = "",
    val album: String? = null,
    val duration: Long = 0L,
    val albumArtUri: String? = null
)
