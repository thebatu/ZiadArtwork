package com.example.ziadartwork.domain.repository

import com.example.ziadartwork.data.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartContent(): Flow<List<CartItem>>
    suspend fun addToCart(paintingId: String): Unit
}