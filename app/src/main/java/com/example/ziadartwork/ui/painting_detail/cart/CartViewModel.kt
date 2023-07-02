package com.example.ziadartwork.ui.painting_detail.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetsnack.model.SnackbarManager
import com.example.ziadartwork.domain.usecases.CartUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    snackbarManager: SnackbarManager,
    private val cartUseCases: CartUseCases
) : ViewModel() {
    private val _cart = MutableStateFlow<Map<String, Int>>(emptyMap())
    val cartState: StateFlow<Map<String, Int>> = _cart.asStateFlow()

    suspend fun getCartContentForPainting(paintingId: String): Int {
        val content = cartUseCases.getCartContent().first()
        return content[paintingId] ?: 0
    }

    fun addPaintingToCart(paintingId: String) {
        viewModelScope.launch {
            cartUseCases.addToCart(paintingId)
        }
    }

    fun decreaseLocalPaintingCount(paintingId: String) {
        val totalItemCount = _cart.value.size
        if (totalItemCount == 1) {
//            removePaintingFromCart(paintingId)
        } else {

        }
    }

    private fun removePaintingFromCart(paintingId: String) {
        val currentCart = _cart.value.toMutableMap()
        currentCart.remove(paintingId)
        _cart.value = currentCart
    }

    private fun updateCartWithNewCount(paintingId: String, count: Int) {
        val currentCart = _cart.value.toMutableMap()
        currentCart[paintingId] = count
        _cart.value = currentCart
    }


}