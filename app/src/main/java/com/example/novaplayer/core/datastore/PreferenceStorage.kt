package com.example.novaplayer.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    fun observeString(
        key: String,
        defaultValue: String
    ): Flow<String> {

        val preferenceKey = stringPreferencesKey(key)

        return dataStore.data.map { preferences ->
            preferences[preferenceKey] ?: defaultValue
        }
    }

    suspend fun setString(
        key: String,
        value: String
    ) {
        val preferenceKey = stringPreferencesKey(key)

        dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }
}