package com.example.novaplayer.features.player.domain.mapper



import com.example.novaplayer.features.home.domain.model.Track
import com.example.novaplayer.features.player.domain.CurrentSong

fun Track.toCurrentSong(): CurrentSong {
    return CurrentSong(
        id = id,
        uri = uri,
        title = title,
        artist = artist,
        album = album,
        duration = duration,
        albumArtUri = albumArtUri
    )
}