package com.example.ziadartwork.ui.shoppingcart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetsnack.model.SnackbarManager
import com.example.ziadartwork.data.model.CartItem
import com.example.ziadartwork.domain.usecases.CartUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "CartViewModel"

@HiltViewModel
class DetailScreenShoppingCartViewModel @Inject constructor(
    snackbarManager: SnackbarManager,
    private val cartUseCases: CartUseCases
) : ViewModel() {

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cartState: StateFlow<List<CartItem>> = _cart.asStateFlow()

    init {
        Log.d(TAG, "CartViewModel Initialized: $this (hashCode: ${hashCode()})")
        viewModelScope.launch {
            cartUseCases.getCartContent().collect { cartContent ->
                _cart.value = cartContent
            }
        }
    }

    fun getCurrentPaintingCount(id: String): StateFlow<Int> {
        // whenever _cart is updated emit a new value
        return _cart.map { carts ->
            carts.find { it.paintingId == id }?.quantity ?: 0
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )
    }

    suspend fun addPainting(paintingId: String) {
        viewModelScope.launch {
            cartUseCases.addToCart(paintingId)
        }
    }

    private fun removePainting(paintingId: String) {
        //call usecase to remove painting from cart
//        val currentCart = _cart.value.toMutableMap()
//        currentCart.remove(paintingId)
//        _cart.value = currentCart
    }


}