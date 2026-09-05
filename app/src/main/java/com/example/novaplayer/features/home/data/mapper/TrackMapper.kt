package com.example.novaplayer.features.home.data.mapper

import com.example.novaplayer.features.home.data.model.TrackDto
import com.example.novaplayer.features.home.domain.model.Track

fun TrackDto.toDomain(): Track {
    return Track(
        id = id,
        title = title,
        artist = artist,
        album = album,
        duration = duration,
        uri = uri,
        albumArtUri = albumArtUri
    )

}
fun Track.toDto(): TrackDto {
    return TrackDto(
        id = id,
        title = title,
        artist = artist,
        album = album,
        duration = duration,
        uri = uri,
        albumArtUri = albumArtUri
    )

}

