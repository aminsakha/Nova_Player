package com.example.novaplayer.features.playlist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.novaplayer.features.playlist.data.local.dao.PlaylistDao
import com.example.novaplayer.features.playlist.data.local.entity.PlaylistTrackEntity
import com.example.novaplayer.features.playlist.data.local.entity.playListEntity

@Database(
    entities = [
        playListEntity::class,
        PlaylistTrackEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playlistDao(): PlaylistDao
}