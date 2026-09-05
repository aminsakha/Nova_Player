package com.example.novaplayer.features.home.data.repository

import com.example.novaplayer.features.home.data.datasource.LocalAudioDataSource
import com.example.novaplayer.features.home.data.mapper.toDomain
import com.example.novaplayer.features.home.domain.model.Track
import com.example.novaplayer.features.home.domain.repository.TrackRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackRepositoryImpl @Inject constructor(
    private val dataSource: LocalAudioDataSource
) : TrackRepository {

    override suspend fun getTrack(uri: String): Track? {
        return dataSource
            .getTrack(uri)
            ?.toDomain()
    }

    override suspend fun getAllTracks(): List<Track> {
        return dataSource
            .getAllTracks()
            .map { it.toDomain() }
    }
}