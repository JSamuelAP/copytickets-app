package com.example.copytickets.ui.login.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.copytickets.EscaneosApplication.Companion.dataStore
import kotlinx.coroutines.flow.map

class DataStoreRepository(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val ID = stringPreferencesKey("id")
        val TOKEN = stringPreferencesKey("token")
    }

    fun getScannerData() = dataStore.data.map { preferences ->
        UserData(preferences[ID].orEmpty(), preferences[TOKEN].orEmpty())
    }

    suspend fun saveScannerData(id: String, token: String) {
        dataStore.edit { preferences ->
            preferences[ID] = id
            preferences[TOKEN] = token
        }
    }
}