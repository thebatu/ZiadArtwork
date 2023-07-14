package com.example.ziadartwork.ui.painting_detail.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetsnack.model.SnackbarManager
import com.example.ziadartwork.domain.usecases.CartUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import javax.inject.Inject
import javax.inject.Singleton

const val TAG = "CartViewModel"

@HiltViewModel
class CartViewModel @Inject constructor(
    snackbarManager: SnackbarManager,
    private val cartUseCases: CartUseCases
) : ViewModel() {

    private val _cart = MutableStateFlow<Map<String, Int>>(emptyMap())
    val cartState: StateFlow<Map<String, Int>> = _cart.asStateFlow()

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
        return _cart.map { it[id] ?: 0 }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )
    }

    suspend fun addPaintingToCart(paintingId: String) {
        viewModelScope.launch {
            cartUseCases.addToCart(paintingId)
        }
    }

    private fun removePaintingFromCart(paintingId: String) {
        val currentCart = _cart.value.toMutableMap()
        currentCart.remove(paintingId)
        _cart.value = currentCart
    }


}