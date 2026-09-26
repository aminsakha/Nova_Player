package com.example.novaplayer.core.datastore.player

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.novaplayer.core.datastore.CurrentTrackIdDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.currentTrackIdDataStore
        by preferencesDataStore(
            name = "currentTrackId"
        )

@Module
@InstallIn(SingletonComponent::class)
object CurrentTrackIdDataStoreModule {

    @Provides
    @Singleton
    @CurrentTrackIdDataStore
    fun provideTrackIdDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.currentTrackIdDataStore
    }
}