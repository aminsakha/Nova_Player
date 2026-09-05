package com.example.novaplayer.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeScreenRoute

@Serializable
data class PlayerScreenRoute(
    val trackId: String
)

@Serializable
data object PlaylistScreenRoute

@Serializable
data class PlaylistDetailRoute(
    val playlistId: Long
)

@Serializable
data class SelectTrackRoute(
    val playlistId: Long
)
