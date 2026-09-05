package com.example.novaplayer.features.playlist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("playlists")
data class playListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val imageUri: String? = null
)