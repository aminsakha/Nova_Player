package com.example.novaplayer.features.player.domain

data class CurrentSong(
    val uri: String,
    val title: String,
    val artist: String,
    val artworkData: ByteArray? = null
)
