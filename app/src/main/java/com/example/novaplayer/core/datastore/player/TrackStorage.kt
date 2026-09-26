package com.example.novaplayer.core.datastore.player

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.novaplayer.core.datastore.CurrentTrackIdDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TrackStorage @Inject constructor(
    @CurrentTrackIdDataStore
    private val dataStore: DataStore<Preferences>
) {

    private val trackIdKey =
        stringPreferencesKey("last_track_id")

    fun observeCurrentTrackId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[trackIdKey].orEmpty()
        }
    }

    suspend fun setTrackId(id: String) {
        dataStore.edit { preferences ->
            preferences[trackIdKey] = id
        }
    }
}