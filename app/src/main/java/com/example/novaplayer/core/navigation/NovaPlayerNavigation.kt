package com.example.novaplayer.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.novaplayer.features.home.presentation.screen.HomeSc
import com.example.novaplayer.features.player.presentation.PlayerScreen
import com.example.novaplayer.features.settings.domain.model.AppLanguage
import com.example.novaplayer.features.settings.presentation.screen.about.AboutScreen

@Composable
fun NovaPlayerNavigation(
    onLanguageSelected: (AppLanguage) -> Unit,
) {
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
                onAboutClick = {
                    navController.navigate(
                        AboutScreenRoute
                    )
                },
                onLanguageSelected = onLanguageSelected
            )
        }
        composable<PlayerScreenRoute> { backStackEntry ->

            val route = backStackEntry.toRoute<PlayerScreenRoute>()

            PlayerScreen(
                trackUri = route.trackId
            )
        }
        composable<AboutScreenRoute> {

            AboutScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }


}