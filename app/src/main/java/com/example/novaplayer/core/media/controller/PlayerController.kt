package com.example.novaplayer.core.media.controller

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.concurrent.futures.await
import androidx.media3.common.MediaItem
import com.example.novaplayer.core.media.service.PlaybackService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


@Singleton
class PlayerController @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var controller: MediaController? = null

    private val mutablePlaybackError =
        MutableStateFlow<String?>(null)

    val playbackError: StateFlow<String?> =
        mutablePlaybackError.asStateFlow()

    private val playerListener = object : Player.Listener {

        override fun onPlayerError(error: PlaybackException) {
            val message =
                error.message ?: "Playback failed"

            mutablePlaybackError.value = message

            Log.e(
                "MEDIA3",
                message,
                error
            )
        }
    }

    //connect to my service
    suspend fun connect() {
        val sessionToken = SessionToken(
            context,
            ComponentName(
                context,
                PlaybackService::class.java
            )
        )
        controller = MediaController.Builder(
            context,
            sessionToken
        ).buildAsync().await().also { mediaController ->
            mediaController.addListener(playerListener)
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

        Log.d("MEDIA3", "playing...")
    }

    fun pause() {
        controller?.pause()
    }

    fun stop() {
        controller?.stop()
        Log.d("MEDIA3", "stopped...")
    }

    fun seekTo(position: Long) {
        controller?.seekTo(position)

    }

    fun isPlaying()=controller?.isPlaying

    fun release() {
        controller?.removeListener(playerListener)
        controller?.release()
        controller = null
    }

    fun playSelectedSong(uri: String) {
        if (uri.isBlank()) {
            reportPlaybackError(
                "Selected song URI is empty"
            )
            return
        }

        val mediaController = controller

        if (mediaController == null) {
            reportPlaybackError(
                "Player is not connected"
            )
            return
        }

        clearPlaybackError()

        try {
            val mediaItem = MediaItem.fromUri(uri)

            mediaController.setMediaItem(mediaItem)
            mediaController.prepare()
            mediaController.play()
        } catch (error: Exception) {
            reportPlaybackError(
                error.message ?: "Unable to play selected song"
            )
        }
    }

    fun playRaw(res: Int) {
        controller?.apply {
            val uri = "android.resource://${context.packageName}/$res".toUri()
            val mediaItem = MediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            prepare()
            play()
        }
    }
    fun clearPlaybackError() {
        mutablePlaybackError.value = null
    }

    private fun reportPlaybackError(message: String) {
        mutablePlaybackError.value = message

        Log.e(
            "MEDIA3",
            message
        )
    }
}