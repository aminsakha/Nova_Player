package com.example.novaplayer.features.home.data.datasource

import com.example.novaplayer.features.home.data.model.TrackDto
import kotlinx.coroutines.flow.Flow

interface LocalAudioDataSource {

    suspend fun getTrack(uri: String): TrackDto?

    suspend fun getAllTracks(): List<TrackDto>
}