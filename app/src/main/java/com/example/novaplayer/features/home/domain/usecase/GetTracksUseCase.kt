package com.example.novaplayer.features.home.domain.usecase

import com.example.novaplayer.features.home.domain.model.Track
import com.example.novaplayer.features.home.domain.repository.TrackRepository
import jakarta.inject.Inject

class GetTracksUseCase @Inject constructor(
    private val repository: TrackRepository
) {

    suspend operator fun invoke(): List<Track> {
        return repository.getTracks()
    }
}