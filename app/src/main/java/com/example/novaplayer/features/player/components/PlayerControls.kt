package com.example.novaplayer.features.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun PlayerControls(
    isPlaying: Boolean,
    currentPositionMs: Long,
    durationMs: Long,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onSeekTo: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        PlaybackSeekBar(
            currentPositionMs = currentPositionMs,
            durationMs = durationMs,
            onSeekTo = onSeekTo
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceEvenly,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPreviousClick,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = android.R.drawable.ic_media_rew
                    ),
                    contentDescription = "Previous song",
                    modifier = Modifier.size(32.dp),
                    tint =
                        MaterialTheme.colorScheme.onBackground
                )
            }

            IconButton(
                onClick = onPlayPauseClick,
                modifier = Modifier.size(72.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isPlaying) {
                            android.R.drawable.ic_media_pause
                        } else {
                            android.R.drawable.ic_media_play
                        }
                    ),
                    contentDescription = if (isPlaying) {
                        "Pause"
                    } else {
                        "Play"
                    },
                    modifier = Modifier.size(36.dp),
                    tint =
                        MaterialTheme.colorScheme.onBackground
                )
            }

            IconButton(
                onClick = onNextClick,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = android.R.drawable.ic_media_ff
                    ),
                    contentDescription = "Next song",
                    modifier = Modifier.size(32.dp),
                    tint =
                        MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun PlaybackSeekBar(
    currentPositionMs: Long,
    durationMs: Long,
    onSeekTo: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val safeDuration =
        durationMs.coerceAtLeast(0L)

    val safePosition =
        currentPositionMs.coerceIn(
            minimumValue = 0L,
            maximumValue = safeDuration
        )

    var sliderPosition by remember {
        mutableStateOf(0f)
    }

    var isDragging by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(
        safePosition,
        safeDuration
    ) {
        if (!isDragging) {
            sliderPosition =
                safePosition.toFloat()
        }
    }

    val maximumSliderValue =
        safeDuration
            .toFloat()
            .coerceAtLeast(1f)

    val displayedPosition =
        if (isDragging) {
            sliderPosition
                .toLong()
                .coerceIn(0L, safeDuration)
        } else {
            safePosition
        }

    val remainingTime =
        (safeDuration - displayedPosition)
            .coerceAtLeast(0L)

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Slider(
            value = sliderPosition.coerceIn(
                minimumValue = 0f,
                maximumValue = maximumSliderValue
            ),
            onValueChange = { newPosition ->
                isDragging = true
                sliderPosition = newPosition
            },
            onValueChangeFinished = {
                if (safeDuration > 0L) {
                    onSeekTo(
                        sliderPosition.toLong()
                    )
                }

                isDragging = false
            },
            valueRange = 0f..maximumSliderValue,
            enabled = safeDuration > 0L,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {
            Text(
                text = formatPlaybackTime(
                    displayedPosition
                ),
                style =
                    MaterialTheme.typography.bodySmall,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = if (safeDuration > 0L) {
                    "-${formatPlaybackTime(remainingTime)}"
                } else {
                    "0:00"
                },
                style =
                    MaterialTheme.typography.bodySmall,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatPlaybackTime(
    timeMs: Long
): String {
    val totalSeconds =
        timeMs.coerceAtLeast(0L) / 1000L

    val hours =
        totalSeconds / 3600L

    val minutes =
        (totalSeconds % 3600L) / 60L

    val seconds =
        totalSeconds % 60L

    return if (hours > 0L) {
        "$hours:${
            minutes.toString().padStart(2, '0')
        }:${
            seconds.toString().padStart(2, '0')
        }"
    } else {
        "$minutes:${
            seconds.toString().padStart(2, '0')
        }"
    }
}