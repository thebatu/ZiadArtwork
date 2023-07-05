package com.example.ziadartwork.ui.paintings

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.di.AppDispatchers
import com.example.ziadartwork.domain.usecases.PaintingsUseCases
import com.example.ziadartwork.ui.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val paintingsUseCase: PaintingsUseCases,
    private val appDispatchers: AppDispatchers,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val TAG = MainActivityViewModel::class.simpleName

    private val _paintingsState = MutableStateFlow<PaintingsUiState<List<Painting>>>(
        PaintingsUiState.Error(
            Throwable()
        )
    )
    val paintingsState: StateFlow<PaintingsUiState<List<Painting>>> = _paintingsState.asStateFlow()

    private var paintingsList = emptyList<Painting>()

    init {
        Log.d(TAG, "ViewModel Initialized: $this (hashCode: ${hashCode()})")
        fetchPaintings()
    }

    fun fetchPaintings() {
        paintingsUseCase
            .getAllPaintings()
            .onEach { result ->
                Log.d(TAG, "Fetching started")
                _paintingsState.update {
                    when (result) {
                        is Result.Error -> PaintingsUiState.Error(result.exception)

                        is Result.Loading -> PaintingsUiState.Loading

                        is Result.Success -> {
                            paintingsList = result.data
                            PaintingsUiState.Success(paintingsList)
                        }
                    }
                }
            }
            .onCompletion { Log.d(TAG, "Fetching paintings complete") }
            .onStart {
                Log.d(TAG, "Fetching paintings started")
                _paintingsState.value = PaintingsUiState.Loading
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = null
            )
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
