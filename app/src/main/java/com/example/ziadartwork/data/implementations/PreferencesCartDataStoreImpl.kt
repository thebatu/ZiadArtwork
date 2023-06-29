package com.example.ziadartwork.data.implementations

import com.example.ziadartwork.domain.repository.CartDataStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey

class PreferencesCartDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : CartDataStore {

    override fun getCartContent(): Flow<Preferences> {
        return dataStore.data
    }

    override suspend fun addToCart(paintingId: String) {
        dataStore.edit { preferences ->
            val currentCount = preferences[intPreferencesKey(paintingId)] ?: 0
            preferences[intPreferencesKey(paintingId)] = currentCount + 1
        }
    }

}