package com.example.novaplayer.core.media.controller

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.concurrent.futures.await
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.novaplayer.core.media.model.PlaybackError
import com.example.novaplayer.core.media.service.PlaybackService
import com.example.novaplayer.features.player.domain.CurrentSong
import com.example.novaplayer.features.player.presentation.PlayerContract
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Singleton

@Singleton
class PlayerController @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val mutableCurrentMediaItem =
        MutableStateFlow<MediaItem?>(null)

    val currentMediaItem: StateFlow<MediaItem?> =
        mutableCurrentMediaItem.asStateFlow()

    private val mutableIsPlaying =
        MutableStateFlow(false)

    val isPlaying: StateFlow<Boolean> =
        mutableIsPlaying.asStateFlow()

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

        override fun onMediaItemTransition(
            mediaItem: MediaItem?,
            reason: Int
        ) {

            mutableCurrentMediaItem.value = mediaItem
        }

        override fun onIsPlayingChanged(
            isPlaying: Boolean
        ) {
            mutableIsPlaying.value = isPlaying
        }


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
        Log.d(TAG, "CONNECT START")

        connectionMutex.withLock {

            if (controller != null) {
                Log.d(TAG, "ALREADY CONNECTED")
                return@withLock
            }

            val sessionToken = SessionToken(
                context,
                ComponentName(
                    context,
                    PlaybackService::class.java
                )
            )

            Log.d(TAG, "Building MediaController...")

            val newController =
                MediaController.Builder(
                    context,
                    sessionToken
                )
                    .buildAsync()
                    .await()

            Log.d(TAG, "MediaController CREATED")

            newController.addListener(playerListener)

            controller = newController

            mutableCurrentMediaItem.value =
                newController.currentMediaItem

            mutableIsPlaying.value =
                newController.isPlaying

            Log.d(TAG, "CONTROLLER ASSIGNED = $controller")
        }
    }

    fun isConnected(): Boolean {
        return controller != null
    }

    fun play() {
        Log.d(TAG, "PLAY → controller = $controller")
        val mediaController =
            controller ?: run {
                reportPlaybackError(
                    PlaybackError.PlayerNotConnected
                )
                return
            }

        try {

            if (
                mediaController.playbackState ==
                Player.STATE_IDLE &&
                mediaController.currentMediaItem != null
            ) {
                mediaController.prepare()
            }

            mediaController.play()

            Log.d(
                TAG,
                "Playing"
            )

        } catch (error: IllegalStateException) {

            reportPlaybackError(
                playbackError =
                    PlaybackError.InvalidPlayerState,
                cause = error
            )
        }
    }

    fun pause() {

        val mediaController =
            controller ?: run {
                reportPlaybackError(
                    PlaybackError.PlayerNotConnected
                )
                return
            }

        mediaController.pause()

        Log.d(
            TAG,
            "Paused"
        )
    }

    fun stop() {

        val mediaController =
            controller ?: run {
                reportPlaybackError(
                    PlaybackError.PlayerNotConnected
                )
                return
            }

        mediaController.stop()

        Log.d(
            TAG,
            "Stopped"
        )
    }

    fun seekTo(
        position: Long
    ) {

        val mediaController =
            controller ?: run {
                reportPlaybackError(
                    PlaybackError.PlayerNotConnected
                )
                return
            }

        val safePosition =
            position.coerceAtLeast(0L)

        mediaController.seekTo(
            safePosition
        )
    }

    fun isPlaying(): Boolean {
        return controller?.isPlaying == true
    }

    fun getCurrentPosition(): Long {
        return controller?.currentPosition ?: 0L
    }

    fun getDuration(): Long {
        val duration =
            controller?.duration ?: 0L

        return if (duration > 0L) {
            duration
        } else {
            0L
        }
    }

    fun hasCurrentMediaItem(): Boolean {
        return controller?.currentMediaItem != null
    }

    fun getCurrentMediaUri(): String? {
        return controller
            ?.currentMediaItem
            ?.localConfiguration
            ?.uri
            ?.toString()
    }

    fun playSelectedSong(
        uri: String,
        song: CurrentSong
    ) {

        if (uri.isBlank()) {
            reportPlaybackError(
                PlaybackError.EmptySongUri
            )
            return
        }

        val mediaController =
            controller ?: run {
                reportPlaybackError(
                    PlaybackError.PlayerNotConnected
                )
                return
            }

        try {

            val mediaItem =
                MediaItem.Builder()
                    .setUri(uri)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(song.title)
                            .setArtist(song.artist)
                            .setArtworkUri(song.albumArtUri?.toUri())
                            .build()
                    )
                    .build()

            mediaController.setMediaItem(
                mediaItem
            )

            mediaController.prepare()
            mediaController.play()

            Log.d(
                TAG,
                "Selected song started: $uri"
            )

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

    fun playPlaylist(
        songs: List<CurrentSong>,
        selectedIndex: Int
    ) {
        val mediaController = controller ?: run {
            reportPlaybackError(
                PlaybackError.PlayerNotConnected
            )
            return
        }

        if (songs.isEmpty()) {
            return
        }

        try {

            val mediaItems = songs.map { song ->

                MediaItem.Builder()
                    .setMediaId(song.id.toString())
                    .setUri(song.uri)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(song.title)
                            .setArtist(song.artist)
                            .setArtworkUri(
                                song.albumArtUri?.toUri()
                            )
                            .build()
                    )
                    .build()
            }

            mediaController.setMediaItems(
                mediaItems,
                selectedIndex,
                0L
            )

            mediaController.prepare()
            mediaController.play()

        } catch (exception: Exception) {

            Log.e(
                TAG,
                "Unable to play playlist",
                exception
            )
        }
    }
    fun isCurrentMediaItem(uri: String): Boolean {
        return controller
            ?.currentMediaItem
            ?.localConfiguration
            ?.uri
            ?.toString() == uri
    }
    fun next() {
        val mediaController = controller ?: run {
            reportPlaybackError(PlaybackError.PlayerNotConnected)
            return
        }

        if (mediaController.hasNextMediaItem()) {
            mediaController.seekToNextMediaItem()
        }
    }

    fun previous() {
        val mediaController = controller ?: run {
            reportPlaybackError(PlaybackError.PlayerNotConnected)
            return
        }

        if (mediaController.hasPreviousMediaItem()) {
            mediaController.seekToPreviousMediaItem()
        }
    }

    fun playRaw(
        res: Int
    ) {

        val mediaController =
            controller ?: run {
                reportPlaybackError(
                    PlaybackError.PlayerNotConnected
                )
                return
            }

        try {

            val uri =
                "android.resource://${context.packageName}/$res"
                    .toUri()

            val mediaItem =
                MediaItem.fromUri(uri)

            mediaController.setMediaItem(
                mediaItem
            )

            mediaController.prepare()
            mediaController.play()

        } catch (error: Exception) {

            Log.e(
                TAG,
                "Unable to play raw resource",
                error
            )

            reportPlaybackError(
                playbackError =
                    PlaybackError.InvalidPlayerState,
                cause = error
            )
        }
    }

    private fun reportPlaybackError(
        playbackError: PlaybackError,
        cause: Throwable? = null
    ) {

        mutablePlaybackErrors.tryEmit(
            playbackError
        )

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

    fun release() {

        val currentController =
            controller ?: return

        currentController.removeListener(
            playerListener
        )

        currentController.release()

        controller = null

        Log.d(
            TAG,
            "Controller released"
        )
    }

    private companion object {
        const val TAG = "MEDIA3"
    }
}