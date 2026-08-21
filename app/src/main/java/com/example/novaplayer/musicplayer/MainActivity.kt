package com.example.novaplayer.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.novaplayer.core.media.presentation.screen.TestPlayer
import com.example.novaplayer.core.navigation.NovaPlayerNavigation
import com.example.novaplayer.core.ui.theme.NovaPlayerTheme
import com.example.novaplayer.features.player.domain.CurrentSong
import com.example.novaplayer.features.player.presentation.PlayerScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MusicPlayerApp()
            val context = LocalContext.current

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
                    artworkData = null
                )
            }

            NovaPlayerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        PlayerScreen(
                            selectedSong = selectedSong
                        )
                    }
                }
            }
        }
    }
}