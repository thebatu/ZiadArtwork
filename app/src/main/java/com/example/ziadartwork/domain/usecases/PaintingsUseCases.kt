package com.example.ziadartwork.domain.usecases

import com.example.ziadartwork.ui.viewmodels.Result
import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.domain.repository.PaintingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaintingsUseCases @Inject constructor(private val repo: PaintingsRepository) {

    operator fun invoke(): Flow<Result<List<Painting>>> = repo.getAllPaintings()
    suspend fun getPainting(id: String): Result<Painting> = repo.getPainting(id)

}
