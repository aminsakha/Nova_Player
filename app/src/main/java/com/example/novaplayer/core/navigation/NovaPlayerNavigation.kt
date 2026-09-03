package com.example.novaplayer.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.novaplayer.features.home.presentation.screen.HomeSc
import com.example.novaplayer.features.player.presentation.PlayerScreen
import com.example.novaplayer.features.playlist.presentation.PlaylistDetailScreen
import com.example.novaplayer.features.playlist.presentation.PlaylistScreen
import com.example.novaplayer.features.playlist.presentation.SelectTrackScreen

@Composable
fun NovaPlayerNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeScreenRoute
    ) {

        composable<HomeScreenRoute> {

            HomeSc(
                onTrackClick = { track ->

                    navController.navigate(
                        PlayerScreenRoute(
                            trackId = track.uri
                        )
                    )
                },
                onPlaylistClick = { playlistId ->
                    navController.navigate(
                        PlaylistDetailRoute(
                            playlistId = playlistId
                        )
                    )
                }
            )
        }

        composable<PlayerScreenRoute> { backStackEntry ->

            val route = backStackEntry.toRoute<PlayerScreenRoute>()

            PlayerScreen(
                trackUri = route.trackId
            )
        }

        composable<PlaylistScreenRoute> {

            PlaylistScreen(
                onPlaylistClick = { playlistId ->

                    navController.navigate(
                        PlaylistDetailRoute(
                            playlistId = playlistId
                        )
                    )
                }
            )
        }

        composable<PlaylistDetailRoute> { backStackEntry ->

            val route = backStackEntry.toRoute<PlaylistDetailRoute>()

            PlaylistDetailScreen(
                playlistId = route.playlistId,

                onBackClick = {
                    navController.popBackStack()
                },

                onAddSongClick = {
                    navController.navigate(
                        SelectTrackRoute(
                            playlistId = route.playlistId
                        )
                    )
                },

                onTrackClick = { track ->
                    navController.navigate(
                        PlayerScreenRoute(
                            trackId = track.uri
                        )
                    )
                }
            )
        }

        composable<SelectTrackRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<SelectTrackRoute>()

            SelectTrackScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onTracksAdded = {
                    navController.popBackStack()
                }
            )
        }
    }
}
