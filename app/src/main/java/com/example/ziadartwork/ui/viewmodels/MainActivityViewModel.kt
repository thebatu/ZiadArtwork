package com.example.ziadartwork.ui.viewmodels

import WhileUiSubscribed
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ziadartwork.di.AppDispatchers
import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.domain.usecases.PaintingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val paintingsUseCase: PaintingsUseCases,
    private val appDispatchers: AppDispatchers,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val TAG = MainActivityViewModel::class.simpleName

    private val _paintingsState = MutableStateFlow<Result<List<Painting>>>(Result.Loading)
    val paintingsState: StateFlow<Result<List<Painting>>> = _paintingsState.asStateFlow()

    private var paintingsList = emptyList<Painting>()

    fun fetchPaintings(): Flow<PaintingsUiState> = paintingsUseCase.getAllPaintings()
        .map { result ->
            when (result) {
                is Result.Error -> PaintingsUiState.Error(result.exception)
                Result.Loading -> PaintingsUiState.Loading

                is Result.Success -> {
                    paintingsList = result.data
                    PaintingsUiState.Success(paintingsList)
                }
            }
        }
        .onCompletion { Log.d(TAG, "Fetching paintings complete") }
        .shareIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            replay = 1,
        )

    suspend fun getPainting(id: String): Painting? {
        val result = paintingsUseCase.getPainting(id)
        return when (result) {
            is Result.Success -> result.data
            else -> null
        }
    }

    sealed class PaintingsUiState {
        object Loading : PaintingsUiState()
        class Error(e: Throwable) : PaintingsUiState()
        class Success(result: List<Painting>) : PaintingsUiState()
    }

}
