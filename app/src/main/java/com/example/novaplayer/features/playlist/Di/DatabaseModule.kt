package com.example.novaplayer.features.playlist.Di

import android.content.Context
import androidx.room.Room
import com.example.novaplayer.features.playlist.data.local.AppDatabase
import com.example.novaplayer.features.playlist.data.local.dao.PlaylistDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePlaylistDao(
        database: AppDatabase
    ): PlaylistDao {
        return database.playlistDao()
    }
}