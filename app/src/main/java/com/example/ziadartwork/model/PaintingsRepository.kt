package com.example.ziadartwork.model

import com.example.ziadartwork.Response
import kotlinx.coroutines.flow.Flow


interface PaintingsRepository {
    fun getAllPaintings(): Flow<Response<List<Painting>>>
}