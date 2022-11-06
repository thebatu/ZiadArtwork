package com.example.ziadartwork.UI

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ziadartwork.Response
import com.example.ziadartwork.model.Painting
import com.example.ziadartwork.usecases.GetPaintings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getPaintingsUseCase: GetPaintings
) : ViewModel() {

    private val _paintingsState = mutableStateOf<Response<List<Painting>>>(Response.Loading)
    open val paintingsState: State<Response<List<Painting>>> = _paintingsState

    init {
        fetchPaintings()
    }

    private fun fetchPaintings() {
        viewModelScope.launch {
            getPaintingsUseCase.invoke().collect { response ->
                _paintingsState.value = response
            }
        }
    }
}