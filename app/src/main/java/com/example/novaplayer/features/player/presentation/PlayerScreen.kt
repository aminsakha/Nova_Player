package com.example.novaplayer.features.player.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.novaplayer.features.player.components.AlbumArtwork
import com.example.novaplayer.features.player.components.PlayerControls
import com.example.novaplayer.features.player.domain.CurrentSong

@Composable
fun PlayerScreen(
    selectedSong: CurrentSong,
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(selectedSong.uri) {
        viewModel.onAction(
            PlayerContract.UiAction.SelectSong(
                song = selectedSong
            )
        )
    }

    PlayerScreenContent(
        uiState = uiState,
        fallbackSong = selectedSong,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun PlayerScreenContent(
    uiState: PlayerContract.UiState,
    fallbackSong: CurrentSong,
    onAction: (PlayerContract.UiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentSong =
        uiState.currentSong ?: fallbackSong

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AlbumArtwork(
            artworkData = currentSong.artworkData,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .aspectRatio(1f)
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        CurrentSongInformation(
            title = currentSong.title,
            artist = currentSong.artist
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        PlayerControls(
            isPlaying =
                uiState.playbackStatus == PlaybackStatus.PLAYING,
            onPlayPauseClick = {
                onAction(
                    PlayerContract.UiAction.PlayPause
                )
            },
            onPreviousClick = {
                onAction(
                    PlayerContract.UiAction.Previous
                )
            },
            onNextClick = {
                onAction(
                    PlayerContract.UiAction.Next
                )
            }
        )
    }
}

@Composable
private fun CurrentSongInformation(
    title: String,
    artist: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title.ifBlank {
                "Unknown song"
            },
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = artist.ifBlank {
                "Unknown artist"
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}