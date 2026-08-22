package com.example.novaplayer.features.home.data.datasource

import com.example.novaplayer.features.home.data.model.TrackDto

interface LocalAudioDataSource {

    suspend fun getTracks(): List<TrackDto>
}