package com.example.novaplayer.core.navigation

import com.example.novaplayer.features.home.domain.model.Track
import kotlinx.serialization.Serializable

@Serializable
object  HomeScreenRoute

@Serializable
object SearchScreenRoute

@Serializable
data class PlayerScreenRoute(
    val trackId: String
)
