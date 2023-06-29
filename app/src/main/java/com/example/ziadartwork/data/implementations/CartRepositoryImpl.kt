package com.example.ziadartwork.data.implementations

import com.example.ziadartwork.domain.repository.CartDataStore
import android.util.Log
import androidx.datastore.preferences.core.emptyPreferences
import com.example.ziadartwork.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDataStore: CartDataStore
): CartRepository {
    private val TAG = "CartRepositoryImpl"

    override fun getCartContent(): Flow<MutableMap<String, Int>> {
        return cartDataStore.getCartContent()
            .catch { exception ->
                Log.e(TAG, "Exception while getting cart content", exception)
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
        cartDataStore.addToCart(paintingId)
        Log.d(TAG, "addToCart: Item Added $paintingId")
    }
        
}