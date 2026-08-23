package com.example.novaplayer.features.home.data.model

import android.net.Uri

data class TrackDto(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String?,
    val duration: Long,
    val uri: String,
    val albumArtUri: Uri?
)