package com.example.ziadartwork.ui.shoppingcart

import androidx.lifecycle.ViewModel
import com.example.jetsnack.model.SnackbarManager
import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.domain.usecases.CartUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ShoppingCartViewModel @Inject constructor(
    snackbarManager: SnackbarManager,
    private val cartUseCases: CartUseCases
) : ViewModel() {

    //content of the cart
    fun addPainting(painting: Painting) {

    }

    fun removePainting(painting: Painting) {

    }

    fun checkout(cartContent: List<Painting>) {

    }




}