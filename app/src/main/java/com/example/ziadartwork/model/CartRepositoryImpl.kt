package com.example.ziadartwork.model

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDataStorePreferences: DataStore<Preferences>
): CartRepository {
    val TAG = "CartRepositoryImpl"

    override fun getCartContent(): Flow<MutableMap<String, Int>> {
        return cartDataStorePreferences.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val content = mutableMapOf<String, Int>()
                preferences.asMap().forEach { entry ->
                    val key = entry.key.toString()
                    val value = entry.value as? Int
                    if (value != null) {
                        content[key] = value
                    }
                }
                Log.d(TAG, "getCartContent: $content")
                content
            }
    }

    override suspend fun addToCart(paintingId: String) {
        cartDataStorePreferences.edit { preferences ->
            val currentCount = preferences[intPreferencesKey(paintingId)] ?: 0
            preferences[intPreferencesKey(paintingId)] = currentCount + 1
            Log.d(TAG, "addToCart: Item Added")
        }
        
    }



}