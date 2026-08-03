package com.example.novaplayer.core.media.service

import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService: MediaSessionService() {
    @Inject
    lateinit var player : ExoPlayer
    private var mediaSession: MediaSession?=null

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        Log.d("MEDIA3","Service created...")
        mediaSession= MediaSession.Builder(this, player)
            .setCallback(object : MediaSession.Callback {

                override fun onPlaybackResumption(
                    mediaSession: MediaSession,
                    controller: MediaSession.ControllerInfo
                ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {

                    return Futures.immediateFuture(
                        MediaSession.MediaItemsWithStartPosition(
                            emptyList(),
                            0,
                            0L
                        )
                    )
                }
            })
            .build()
    }
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.release()
        player.release()
        super.onDestroy()
    }
}