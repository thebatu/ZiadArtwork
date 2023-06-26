package com.example.ziadartwork.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetsnack.model.SnackbarManager
import com.example.ziadartwork.domain.repository.CartRepository
import com.example.ziadartwork.domain.usecases.CartUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    snackbarManager: SnackbarManager,
    private val cartUseCases: CartUseCases
) : ViewModel() {
    private val _cart = MutableStateFlow<Map<String, Int>>(emptyMap())
    val cartState: StateFlow<Map<String, Int>> = _cart.asStateFlow()

    init {
        viewModelScope.launch {
            cartUseCases.getCartContent().collect { cartContent ->
                // Handle the cartContent here
                Log.d("MyTag", "Cart content: $cartContent")
            }
        }
    }

    fun addPaintingToCart(paintingId: String) {
        viewModelScope.launch {
            cartUseCases.addToCart(paintingId)
        }
        val currentCount: Map<String, Int> = _cart.value
        updateCartWithNewCount(paintingId, currentCount.size + 1)
    }

    fun decreaseLocalPaintingCount(paintingId: String) {
        val totalItemCount = _cart.value.size
        if (totalItemCount == 1) {
            removePaintingFromCart(paintingId)
        } else {
            // update quantity in cart
            updateCartWithNewCount(paintingId, totalItemCount - 1)
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