package com.example.ziadartwork.ui.paintings

import WhileUiSubscribed
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ziadartwork.di.AppDispatchers
import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.domain.usecases.PaintingsUseCases
import com.example.ziadartwork.ui.Result
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
    private val _paintingsState = MutableStateFlow<PaintingsUiState<List<Painting>>>(PaintingsUiState.Loading)
    val paintingsState: StateFlow<PaintingsUiState<List<Painting>>> = _paintingsState.asStateFlow()
    private var paintingsList = emptyList<Painting>()

    init {
        Log.d(TAG, "ViewModel Initialized: $this (hashCode: ${hashCode()})")
        fetchPaintings()
    }

    fun fetchPaintings() {
        Log.d(TAG, "fetchPaintings Called")
        paintingsUseCase.getAllPaintings()
            .map { result ->
                _paintingsState.update {
                    when (result) {
                        is Result.Error -> {
                            Log.d(TAG, "ERROR ")
                            PaintingsUiState.Error(result.exception)
                        }

                        is Result.Loading -> {
                            Log.d(TAG, "LOADING")
                            PaintingsUiState.Loading
                        }

                        is Result.Success -> {
                            Log.d(TAG, "SUCCESS")
                            paintingsList = result.data
                            println(TAG + paintingsList)
                            PaintingsUiState.Success(paintingsList)
                        }
                    }

                }
            }
            .onCompletion { Log.d(TAG, "Fetching paintings complete") }
            .onStart { Log.d(TAG, "Fetching paintings started") }
            .launchIn(viewModelScope)

    }

    suspend fun getPainting(id: String): Painting? {
        return when (val result = paintingsUseCase.getPainting(id)) {
            is Result.Success -> result.data
            else -> null
        }
    }

    sealed class PaintingsUiState<out T> {
        object Loading : PaintingsUiState<Nothing>()
        data class Error(val e: Throwable) : PaintingsUiState<Nothing>()
        data class Success<out T>(val data: T) : PaintingsUiState<T>()
    }

}
