package com.example.novaplayer.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.novaplayer.core.media.presentation.screen.PlayerScreen
import com.example.novaplayer.core.ui.theme.NovaPlayerTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.platform.LocalContext
import com.example.novaplayer.R

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current

            val selectedSongUri =
                "android.resource://${context.packageName}/${R.raw.vinak}"
            NovaPlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {

                        PlayerScreen(
                            songUri = selectedSongUri
                        )
                    }
                }
            }
        }
    }
}

