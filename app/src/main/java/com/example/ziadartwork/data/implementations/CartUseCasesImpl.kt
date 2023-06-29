package com.example.ziadartwork.data.implementations

import com.example.ziadartwork.domain.repository.CartRepository
import com.example.ziadartwork.domain.usecases.CartUseCases
import javax.inject.Inject

class CartUseCasesImpl @Inject constructor(private val cartRepository: CartRepository) :
    CartUseCases {

    override fun getCartContent() = cartRepository.getCartContent()
    override suspend fun addToCart(paintingId: String) = cartRepository.addToCart(paintingId)
    override suspend fun removeFromCart(paintingId: String) = TODO()

}
