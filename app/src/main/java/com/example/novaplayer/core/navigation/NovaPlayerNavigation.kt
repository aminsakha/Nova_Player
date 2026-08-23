package com.example.novaplayer.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.novaplayer.features.home.presentation.screen.HomeSc
import com.example.novaplayer.features.player.presentation.PlayerScreen

@Composable
fun NovaPlayerNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeScreenRoute
    ) {
        composable<HomeScreenRoute> {
            HomeSc { track ->

                navController.navigate(
                    PlayerScreenRoute(
                        trackId = track.uri
                    )
                )
            }
        }
        composable<PlayerScreenRoute> { backStackEntry ->

            val route = backStackEntry.toRoute<PlayerScreenRoute>()

            PlayerScreen(
                trackUri = route.trackId
            )
        }
    }


}