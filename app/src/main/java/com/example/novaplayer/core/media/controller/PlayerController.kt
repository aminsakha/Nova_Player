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


@Singleton
class PlayerController @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var controller: MediaController? = null

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
        ).buildAsync().await()
        Log.d("MEDIA3", "controller created...")

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
        controller?.release()
        controller = null
    }

    fun playSelectedSong(uri: String) {
        if (uri.isBlank()) {
            Log.e("MEDIA3", "Selected song uri is blank")
            return
        }

        controller?.apply {
            val mediaItem = MediaItem.fromUri(uri)

            setMediaItem(mediaItem)
            prepare()
            play()
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
}