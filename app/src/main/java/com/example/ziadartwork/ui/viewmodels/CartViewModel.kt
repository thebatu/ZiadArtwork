package com.example.ziadartwork.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetsnack.model.SnackbarManager
import com.example.ziadartwork.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    snackbarManager: SnackbarManager,
    private val cartRepository: CartRepository
) : ViewModel() {
    private val _cart = MutableStateFlow<Map<String, Int>>(emptyMap())
    val cartState: StateFlow<Map<String, Int>> = _cart.asStateFlow()

    init {
        viewModelScope.launch {
            cartRepository.getCartContent().collect { cartContent ->
                // Handle the cartContent here
                Log.d("MyTag", "Cart content: $cartContent")
            }
        }
    }

    fun increasePaintingCount(paintingId: String) {
        viewModelScope.launch {
            cartRepository.addToCart(paintingId)
        }
            val currentCount: Map<String, Int> = _cart.value
            updatePaintingCount(paintingId, currentCount.size + 1)
    }

    fun decreaseSnackCount(paintingId: String) {
            val currentCount = _cart.value.size
            if (currentCount == 1) {
                removeSnack(paintingId)
            } else {
                // update quantity in cart
                updatePaintingCount(paintingId, currentCount - 1)
            }
        }

    private fun removeSnack(paintingId: String) {
        val currentCart = _cart.value.toMutableMap()
        currentCart.remove(paintingId)
        _cart.value = currentCart
    }

    private fun updatePaintingCount(paintingId: String, count: Int) {
        val currentCart = _cart.value.toMutableMap()
        currentCart[paintingId] = count
        _cart.value = currentCart
    }


}