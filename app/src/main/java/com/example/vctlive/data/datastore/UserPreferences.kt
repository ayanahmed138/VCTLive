package com.example.vctlive.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.dataStore by preferencesDataStore(
    name = "user_preferences"
)


class UserPreferences(
    private val context: Context
) {

    companion object {
        private val FOLLOWED_MATCHES =
            stringSetPreferencesKey("followed_matches")
    }


    val followedMatches: Flow<Set<String>> =
        context.dataStore.data.map { preferences ->
            preferences[FOLLOWED_MATCHES] ?: emptySet()
        }


    suspend fun followMatch(matchId: String) {

        context.dataStore.edit { preferences ->

            val current =
                preferences[FOLLOWED_MATCHES] ?: emptySet()

            preferences[FOLLOWED_MATCHES] =
                current + matchId
        }
    }


    suspend fun unfollowMatch(matchId: String) {

        context.dataStore.edit { preferences ->

            val current =
                preferences[FOLLOWED_MATCHES] ?: emptySet()

            preferences[FOLLOWED_MATCHES] =
                current - matchId
        }
    }
}