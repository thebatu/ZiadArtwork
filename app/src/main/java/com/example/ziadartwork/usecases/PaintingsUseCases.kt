package com.example.ziadartwork.usecases

import com.example.ziadartwork.Result
import com.example.ziadartwork.model.Painting
import com.example.ziadartwork.model.PaintingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaintingsUseCases @Inject constructor(private val repo: PaintingsRepository) {

    operator fun invoke(): Flow<Result<List<Painting>>> = repo.getAllPaintings()
    suspend fun getPainting(id: String): Result<Painting> = repo.getPainting(id)

}
