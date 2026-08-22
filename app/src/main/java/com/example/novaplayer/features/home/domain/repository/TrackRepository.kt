package com.example.novaplayer.features.home.domain.repository

import com.example.novaplayer.features.home.domain.model.Track

interface TrackRepository {
    suspend fun getTracks():List<Track>
}