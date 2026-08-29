package com.example.novaplayer.features.search.domain.usecase

import com.example.novaplayer.features.home.domain.model.Track
import javax.inject.Inject

class SearchTracksUseCase @Inject constructor() {

    operator fun invoke(
        tracks: List<Track>,
        query: String
    ): List<Track> {
        val normalizedQuery = query.trim()

        if (normalizedQuery.isEmpty()) {
            return tracks
        }

        return tracks.filter { track ->
            track.title.contains(
                other = normalizedQuery,
                ignoreCase = true
            ) ||
                    track.artist.contains(
                        other = normalizedQuery,
                        ignoreCase = true
                    ) ||
                    track.album.orEmpty().contains(
                        other = normalizedQuery,
                        ignoreCase = true
                    )
        }
    }
}