package com.example.ziadartwork.data.repository

import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.domain.repository.CartRepository
import com.example.ziadartwork.domain.usecases.CartUseCases
import javax.inject.Inject

class CartUseCasesImpl @Inject constructor(private val cartRepository: CartRepository) :
    CartUseCases {

    override fun getCartContent() = cartRepository.getCartContent()
    override suspend fun addToCart(paintingId: String) = cartRepository.addToCart(paintingId)
    override suspend fun removeFromCart(paintingId: String) = TODO()
    override suspend fun getAllPaintings(): List<Painting> {
        TODO("Not yet implemented")
    }

}
    