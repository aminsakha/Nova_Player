package com.example.novaplayer.features.home.domain.repository

import com.example.novaplayer.features.home.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {

    suspend fun getTrack(uri: String): Track?

    suspend fun getAllTracks(): List<Track>
}