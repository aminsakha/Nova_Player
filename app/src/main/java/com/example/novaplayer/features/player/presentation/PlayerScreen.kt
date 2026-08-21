package com.example.novaplayer.features.player.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.novaplayer.R
import com.example.novaplayer.features.player.components.AlbumArtwork
import com.example.novaplayer.features.player.components.PlayerControls
import com.example.novaplayer.features.player.domain.CurrentSong
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember

@Composable
fun PlayerScreen(
    selectedSong: CurrentSong,
    isFavorite: Boolean = false,
    onFavoriteClick: (CurrentSong) -> Unit = { _ -> },
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState =
        remember {
            SnackbarHostState()
        }

    LaunchedEffect(selectedSong.uri) {
        viewModel.onAction(
            PlayerContract.UiAction.SelectSong(
                song = selectedSong
            )
        )
    }

    LaunchedEffect(uiState.errorMessage) {
        val errorMessage =
            uiState.errorMessage
                ?: return@LaunchedEffect

        snackbarHostState.showSnackbar(
            message = errorMessage
        )

        viewModel.onAction(
            PlayerContract.UiAction.ClearError
        )
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        PlayerScreenContent(
            uiState = uiState,
            fallbackSong = selectedSong,
            isFavorite = isFavorite,
            onFavoriteClick = onFavoriteClick,
            onAction = viewModel::onAction,
            modifier = Modifier.fillMaxSize()
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                )
        )
    }
}

@Composable
private fun PlayerScreenContent(
    uiState: PlayerContract.UiState,
    fallbackSong: CurrentSong,
    isFavorite: Boolean,
    onFavoriteClick: (CurrentSong) -> Unit,
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
            modifier = Modifier.weight(1f)
        )

        CurrentSongInformation(
            title = currentSong.title,
            artist = currentSong.artist,
            isFavorite = isFavorite,
            onFavoriteClick = {
                onFavoriteClick(currentSong)
            }
        )



        PlayerControls(
            isPlaying =
                uiState.playbackStatus ==
                        PlaybackStatus.PLAYING,
            currentPositionMs =
                uiState.currentPositionMs,
            durationMs =
                uiState.durationMs,
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
            },
            onSeekTo = { positionMs ->
                onAction(
                    PlayerContract.UiAction.SeekTo(
                        positionMs = positionMs
                    )
                )
            }
        )
    }
}

@Composable
private fun CurrentSongInformation(
    title: String,
    artist: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title.ifBlank {
                    "Unknown song"
                },
                style =
                    MaterialTheme.typography.headlineSmall,
                color =
                    MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = artist.ifBlank {
                    "Unknown artist"
                },
                style =
                    MaterialTheme.typography.bodyLarge,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }

        IconButton(
            onClick = onFavoriteClick
        ) {
            Icon(
                painter = painterResource(
                    id = if (isFavorite) {
                        R.drawable.ic_favorite_filled
                    } else {
                        R.drawable.ic_favorite_border
                    }
                ),
                contentDescription = if (isFavorite) {
                    "Remove from favorites"
                } else {
                    "Add to favorites"
                },
                tint =
                    MaterialTheme.colorScheme.onBackground
            )
        }
    }
}