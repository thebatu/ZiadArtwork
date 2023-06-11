package com.example.ziadartwork.model

import com.example.ziadartwork.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface CartRepository {
    fun getCartContent(): Flow<MutableMap<String, Int>>

    suspend fun addToCart(paintingId: String): Unit
}