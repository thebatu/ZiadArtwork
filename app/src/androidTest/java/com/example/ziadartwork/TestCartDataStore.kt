package com.example.ziadartwork

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.example.ziadartwork.domain.repository.CartDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestCartDataStore : CartDataStore {
    private val _data = MutableStateFlow<Preferences>(emptyPreferences())

    override fun getCartContent(): Flow<Preferences> = _data

    override suspend fun addToCart(paintingId: String) {

    }
}
