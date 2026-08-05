package com.example.novaplayer.core.media.presentation.screen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.novaplayer.core.media.presentation.contract.Media3Contract
import com.example.novaplayer.core.media.presentation.contract.PlayState
import com.example.novaplayer.core.media.presentation.viewmodel.PlayerViewModel
import com.example.novaplayer.core.ui.theme.ShapeMedium
import com.example.novaplayer.core.ui.theme.Space8
import com.example.novaplayer.R

@Composable
fun TestPlayer(
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val selectedSongUri =
        "android.resource://${context.packageName}/${R.raw.vinak}"

    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )

    val iconScale = if (uiState.playState == PlayState.playing) {
        scale
    } else {
        1f
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = "🎵",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.scale(iconScale)
        )


        Spacer(modifier = Modifier.height(Space8))


        Button(
            modifier = Modifier.fillMaxWidth(0.5f),
            shape = ShapeMedium,
            enabled = uiState.playState != PlayState.playing,
            onClick = {
                viewModel.onAction(
                    Media3Contract.UiAction.PlaySelectedSong(
                        uri = selectedSongUri
                    )
                )
            }
        ) {
            Text("Play")
        }


        Button(
            modifier = Modifier.fillMaxWidth(0.5f),
            shape = ShapeMedium,
            enabled = uiState.playState == PlayState.playing,
            onClick = {
                viewModel.onAction(
                    Media3Contract.UiAction.pause
                )
            }
        ) {
            Text("Pause")
        }
    }
}