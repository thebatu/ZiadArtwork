package com.example.ziadartwork.domain.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface CartDataStore {
    fun getCartContent(): Flow<Preferences>
    suspend fun addToCart(paintingId: String)
}
