package com.example.novaplayer.core.media.controller

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.concurrent.futures.await
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.novaplayer.core.media.model.PlaybackError
import com.example.novaplayer.core.media.service.PlaybackService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PlayerController @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var controller: MediaController? = null

    private val connectionMutex = Mutex()

    private val mutablePlaybackErrors =
        MutableSharedFlow<PlaybackError>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    val playbackErrors: SharedFlow<PlaybackError> =
        mutablePlaybackErrors.asSharedFlow()

    private val playerListener = object : Player.Listener {

        override fun onPlayerError(
            error: PlaybackException
        ) {
            reportPlaybackError(
                playbackError = PlaybackError.PlaybackFailed(
                    errorCode = error.errorCode
                ),
                cause = error
            )
        }
    }

    suspend fun connect() {
        connectionMutex.withLock {
            if (controller != null) {
                return@withLock
            }

            val sessionToken = SessionToken(
                context,
                ComponentName(
                    context,
                    PlaybackService::class.java
                )
            )

            val newController =
                MediaController.Builder(
                    context,
                    sessionToken
                ).buildAsync().await()

            newController.addListener(playerListener)
            controller = newController

            Log.d(TAG, "Controller connected")
        }
    }

    fun play() {
        controller?.apply {
            if (
                playbackState == Player.STATE_IDLE &&
                currentMediaItem != null
            ) {
                prepare()
            }

            play()
        }

        Log.d(TAG, "Playing")
    }

    fun pause() {
        controller?.pause()
    }

    fun stop() {
        controller?.stop()

        Log.d(TAG, "Stopped")
    }

    fun seekTo(position: Long) {
        controller?.seekTo(position)
    }

    fun isPlaying(): Boolean {
        return controller?.isPlaying ?: false
    }

    fun playSelectedSong(uri: String) {
        if (uri.isBlank()) {
            reportPlaybackError(
                playbackError =
                    PlaybackError.EmptySongUri
            )
            return
        }

        val mediaController = controller

        if (mediaController == null) {
            reportPlaybackError(
                playbackError =
                    PlaybackError.PlayerNotConnected
            )
            return
        }

        try {
            val mediaItem = MediaItem.fromUri(uri)

            mediaController.setMediaItem(mediaItem)
            mediaController.prepare()
            mediaController.play()
        } catch (error: IllegalArgumentException) {
            reportPlaybackError(
                playbackError =
                    PlaybackError.InvalidSongUri,
                cause = error
            )
        } catch (error: SecurityException) {
            reportPlaybackError(
                playbackError =
                    PlaybackError.PermissionDenied,
                cause = error
            )
        } catch (error: IllegalStateException) {
            reportPlaybackError(
                playbackError =
                    PlaybackError.InvalidPlayerState,
                cause = error
            )
        } catch (error: RuntimeException) {
            Log.e(
                TAG,
                "Unexpected error while playing selected song",
                error
            )

            throw error
        }
    }

    fun playRaw(res: Int) {
        controller?.apply {
            val uri =
                "android.resource://${context.packageName}/$res"
                    .toUri()

            val mediaItem = MediaItem.fromUri(uri)

            setMediaItem(mediaItem)
            prepare()
            play()
        }
    }

    fun release() {
        val currentController = controller ?: return

        currentController.removeListener(playerListener)
        currentController.release()
        controller = null

        Log.d(TAG, "Controller released")
    }

    private fun reportPlaybackError(
        playbackError: PlaybackError,
        cause: Throwable? = null
    ) {
        mutablePlaybackErrors.tryEmit(playbackError)

        if (cause != null) {
            Log.e(
                TAG,
                playbackError.toString(),
                cause
            )
        } else {
            Log.e(
                TAG,
                playbackError.toString()
            )
        }
    }

    private companion object {
        const val TAG = "MEDIA3"
    }
}