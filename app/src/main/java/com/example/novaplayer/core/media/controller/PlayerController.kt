package com.example.novaplayer.core.media.controller

import android.content.ComponentName
import android.content.Context
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.concurrent.futures.await
import com.example.novaplayer.core.media.service.PlaybackService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PlayerController @Inject constructor(
    @ApplicationContext private val context: Context
){
    private var controller: MediaController?=null
    //connect to my service
    suspend fun connect(){
        val sessionToken= SessionToken(
            context,
            ComponentName(
                context,
                PlaybackService::class.java
            )
        )
        controller= MediaController.Builder(
            context,
            sessionToken
        ).buildAsync().await()

    }
    fun play(){
        controller?.play()
    }
    fun pause(){
        controller?.pause()
    }
    fun seekTo(position:Long){
        controller?.seekTo(position)
    }
    fun release(){
        controller?.release()
        controller=null
    }
}