package com.example.ziadartwork.domain.usecases

import kotlinx.coroutines.flow.Flow

interface CartUseCases {

    fun getCartContent(): Flow<Map<String, Int>>
    suspend fun addToCart(paintingId: String)
    suspend fun removeFromCart(paintingId: String)

}