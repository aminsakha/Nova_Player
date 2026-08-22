package com.example.novaplayer.features.home.data.repository

import com.example.novaplayer.features.home.data.datasource.LocalAudioDataSource
import com.example.novaplayer.features.home.data.mapper.toDomain
import com.example.novaplayer.features.home.domain.model.Track
import com.example.novaplayer.features.home.domain.repository.TrackRepository
import jakarta.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val dataSource: LocalAudioDataSource
) : TrackRepository {

    override suspend fun getTracks(): List<Track> {
        return dataSource
            .getTracks()
            .map { it.toDomain() }
    }
}