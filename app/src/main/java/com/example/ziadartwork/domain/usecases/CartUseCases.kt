package com.example.ziadartwork.domain.usecases

import com.example.ziadartwork.data.model.CartItem
import com.example.ziadartwork.data.model.Painting
import kotlinx.coroutines.flow.Flow

interface CartUseCases {

    fun getCartContent() : Flow<List<CartItem>>
    suspend fun addToCart(paintingId: String)
    suspend fun removeFromCart(paintingId: String)
    suspend fun getAllPaintings() : List<Painting>

}