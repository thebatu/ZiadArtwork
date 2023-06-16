package com.example.ziadartwork.domain.repository

import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartContent(): Flow<MutableMap<String, Int>>

    suspend fun addToCart(paintingId: String): Unit
}