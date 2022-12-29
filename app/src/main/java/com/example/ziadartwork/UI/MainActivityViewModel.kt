package com.example.ziadartwork.UI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.ziadartwork.AppDispatchers
import com.example.ziadartwork.Response
import com.example.ziadartwork.model.Painting
import com.example.ziadartwork.usecases.GetPaintingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getPaintingsUseCase: GetPaintingsUseCase,
    private val appDispatchers: AppDispatchers,
) : ViewModel() {

    private val TAG = MainActivityViewModel::class.simpleName
    private val _paintingsState = MutableStateFlow<Response<List<Painting>>>(Response.Loading)
    val paintingsState: StateFlow<Response<List<Painting>>> = _paintingsState.asStateFlow()

    init {
        fetchPaintings()
    }

    fun fetchPaintings() {
        viewModelScope.launch {
            try {
                getPaintingsUseCase.invoke().collectLatest { response ->
                    _paintingsState.value = response
                    println("HELLO $response")
                }
                val s = ""
            } catch (e: IOException) {
                _paintingsState.value = Response.Failure(e)
            } catch (e: HttpException) {
                _paintingsState.value = Response.Failure(e)
            }

        }
    }

    sealed class UiState {
        object Loading : UiState()
        class Error(e: Throwable) : UiState()
        class Success(result: List<Painting>) : UiState()

    }
}

