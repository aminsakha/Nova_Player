package com.example.novaplayer.features.player.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
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

    var draggedPosition by remember {
        mutableStateOf<Long?>(null)
    }

    val displayedPosition =
        draggedPosition ?: safePosition

    val progress =
        if (safeDuration > 0L) {
            displayedPosition.toFloat() /
                    safeDuration.toFloat()
        } else {
            0f
        }.coerceIn(0f, 1f)

    val activeColor =
        MaterialTheme.colorScheme.primary

    val inactiveColor =
        MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.30f
        )

    val thumbBorderColor =
        MaterialTheme.colorScheme.background

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .pointerInput(safeDuration) {
                    if (safeDuration <= 0L) {
                        return@pointerInput
                    }

                    awaitEachGesture {
                        val down =
                            awaitFirstDown(
                                requireUnconsumed = false
                            )

                        var targetPosition =
                            calculateSeekPosition(
                                touchPositionX =
                                    down.position.x,
                                trackWidth =
                                    size.width.toFloat(),
                                durationMs =
                                    safeDuration
                            )

                        draggedPosition =
                            targetPosition

                        down.consume()

                        var pointerIsPressed = true

                        while (pointerIsPressed) {
                            val event =
                                awaitPointerEvent()

                            val change =
                                event.changes.firstOrNull()

                            if (change == null) {
                                pointerIsPressed = false
                            } else {
                                targetPosition =
                                    calculateSeekPosition(
                                        touchPositionX =
                                            change.position.x,
                                        trackWidth =
                                            size.width.toFloat(),
                                        durationMs =
                                            safeDuration
                                    )

                                draggedPosition =
                                    targetPosition

                                pointerIsPressed =
                                    change.pressed

                                change.consume()
                            }
                        }

                        onSeekTo(targetPosition)
                        draggedPosition = null
                    }
                }
        ) {
            val centerY =
                size.height / 2f

            val progressX =
                size.width * progress

            val trackStrokeWidth =
                4.dp.toPx()

            drawLine(
                color = inactiveColor,
                start = Offset(
                    x = 0f,
                    y = centerY
                ),
                end = Offset(
                    x = size.width,
                    y = centerY
                ),
                strokeWidth = trackStrokeWidth,
                cap = StrokeCap.Round
            )

            drawLine(
                color = activeColor,
                start = Offset(
                    x = 0f,
                    y = centerY
                ),
                end = Offset(
                    x = progressX,
                    y = centerY
                ),
                strokeWidth = trackStrokeWidth,
                cap = StrokeCap.Round
            )

            drawCircle(
                color = thumbBorderColor,
                radius = 6.dp.toPx(),
                center = Offset(
                    x = progressX,
                    y = centerY
                )
            )

            drawCircle(
                color = activeColor,
                radius = 5.dp.toPx(),
                center = Offset(
                    x = progressX,
                    y = centerY
                )
            )
        }

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

            val remainingTime =
                (safeDuration - displayedPosition)
                    .coerceAtLeast(0L)

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

private fun calculateSeekPosition(
    touchPositionX: Float,
    trackWidth: Float,
    durationMs: Long
): Long {
    if (
        trackWidth <= 0f ||
        durationMs <= 0L
    ) {
        return 0L
    }

    val progress =
        (touchPositionX / trackWidth)
            .coerceIn(0f, 1f)

    return (
            durationMs.toDouble() *
                    progress.toDouble()
            ).toLong()
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