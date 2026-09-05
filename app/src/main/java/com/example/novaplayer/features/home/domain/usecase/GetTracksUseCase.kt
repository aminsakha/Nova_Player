package com.example.novaplayer.features.home.domain.usecase

import com.example.novaplayer.features.home.domain.model.Track
import com.example.novaplayer.features.home.domain.repository.TrackRepository
import jakarta.inject.Inject

class GetTracksUseCase @Inject constructor(
    private val repository: TrackRepository
) {

    suspend  fun getAllTrack(): List<Track> {
        return repository.getAllTracks()
    }
    suspend fun getTrack(uri:String): Track?{
        return repository.getTrack(uri)
    }
}