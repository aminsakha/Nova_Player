package com.example.novaplayer.musicplayer

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.novaplayer.R
import com.example.novaplayer.core.navigation.NovaPlayerNavigation
import com.example.novaplayer.core.ui.theme.NovaPlayerTheme
import com.example.novaplayer.features.player.domain.CurrentSong
import com.example.novaplayer.features.settings.domain.model.ThemeMode
import com.example.novaplayer.features.settings.presentation.viewmodel.SettingsViewModel
//import androidx.compose.ui.platform.LocalLayoutDirection
//import androidx.compose.ui.unit.LayoutDirection
//import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun MusicPlayerApp() {
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val uiState by settingsViewModel.uiState.collectAsState()

    val darkTheme = when (uiState.theme) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    var isSettingsOpen by remember {
        mutableStateOf(false)
    }
    val selectedSong = remember(
        context.packageName
    ) {
        CurrentSong(
            uri =
                "android.resource://" +
                        "${context.packageName}/" +
                        "${R.raw.vinak}",
            title = "Vinak",
            artist = "Karam Hite",
            albumArtUri = ""
        )
    }
    // Forcing Compose to stay LTR
//    CompositionLocalProvider(
//        LocalLayoutDirection provides LayoutDirection.Ltr
//    ) {
        NovaPlayerTheme(
            darkTheme = darkTheme
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {

                    NovaPlayerNavigation()
                }
            }
        }
//    }

}