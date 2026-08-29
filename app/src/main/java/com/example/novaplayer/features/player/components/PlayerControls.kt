package com.example.novaplayer.features.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api

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
                    imageVector =
                        Icons.Filled.FastRewind,
                    contentDescription =
                        "Previous song",
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
                    imageVector =
                        if (isPlaying) {
                            Icons.Filled.Pause
                        } else {
                            Icons.Filled.PlayArrow
                        },
                    contentDescription =
                        if (isPlaying) {
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
                    imageVector =
                        Icons.Filled.FastForward,
                    contentDescription =
                        "Next song",
                    modifier = Modifier.size(32.dp),
                    tint =
                        MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        if (safeDuration > 0L) {
            currentPositionMs.coerceIn(
                minimumValue = 0L,
                maximumValue = safeDuration
            )
        } else {
            0L
        }

    var draggedPosition by remember(
        safeDuration
    ) {
        mutableStateOf<Long?>(null)
    }

    val displayedPosition =
        draggedPosition ?: safePosition

    val maximumSliderValue =
        safeDuration
            .toFloat()
            .coerceAtLeast(1f)

    val sliderEnabled =
        safeDuration > 0L

    val sliderColors =
        SliderDefaults.colors(
            thumbColor =
                MaterialTheme.colorScheme.primary,
            activeTrackColor =
                MaterialTheme.colorScheme.primary,
            inactiveTrackColor =
                MaterialTheme.colorScheme
                    .onSurface
                    .copy(alpha = 0.30f),
            disabledThumbColor =
                MaterialTheme.colorScheme.primary
                    .copy(alpha = 0.38f),
            disabledActiveTrackColor =
                MaterialTheme.colorScheme.primary
                    .copy(alpha = 0.38f),
            disabledInactiveTrackColor =
                MaterialTheme.colorScheme
                    .onSurface
                    .copy(alpha = 0.12f)
        )

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Slider(
            value =
                displayedPosition
                    .toFloat()
                    .coerceIn(
                        minimumValue = 0f,
                        maximumValue =
                            maximumSliderValue
                    ),
            onValueChange = { newPosition ->
                draggedPosition =
                    newPosition
                        .toLong()
                        .coerceIn(
                            minimumValue = 0L,
                            maximumValue =
                                safeDuration
                        )
            },
            onValueChangeFinished = {
                draggedPosition?.let {
                        selectedPosition ->

                    onSeekTo(selectedPosition)
                }

                draggedPosition = null
            },
            valueRange =
                0f..maximumSliderValue,
            enabled = sliderEnabled,
            colors = sliderColors,
            thumb = {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            color =
                                if (sliderEnabled) {
                                    MaterialTheme
                                        .colorScheme
                                        .primary
                                } else {
                                    MaterialTheme
                                        .colorScheme
                                        .primary
                                        .copy(alpha = 0.38f)
                                },
                            shape = CircleShape
                        )
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier =
                        Modifier.height(4.dp),
                    enabled = sliderEnabled,
                    colors = sliderColors,
                    drawStopIndicator = null,
                    thumbTrackGapSize = 0.dp,
                    trackInsideCornerSize = 2.dp
                )
            },
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
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
            )

            val remainingTime =
                (safeDuration - displayedPosition)
                    .coerceAtLeast(0L)

            Text(
                text =
                    if (safeDuration > 0L) {
                        "-${
                            formatPlaybackTime(
                                remainingTime
                            )
                        }"
                    } else {
                        "0:00"
                    },
                style =
                    MaterialTheme.typography.bodySmall,
                color =
                    MaterialTheme.colorScheme
                        .onSurfaceVariant
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