package com.example.spaceflightnewsapp.data.preference

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferenceDataStore(private val context: Context) {

    companion object {
        private val SOURCE_KEY = stringPreferencesKey("news_source")
    }

    val selectedSource: Flow<String?> =
        context.dataStore.data.map { it[SOURCE_KEY] }

    suspend fun saveSource(source: String?) {
        context.dataStore.edit { prefs ->
            if (source == null) prefs.remove(SOURCE_KEY)
            else prefs[SOURCE_KEY] = source
        }
    }
}
