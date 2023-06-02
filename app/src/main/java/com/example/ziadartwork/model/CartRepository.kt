package com.example.ziadartwork.model

import com.example.ziadartwork.Result
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartContent(): Flow<<List<Painting>>>
}