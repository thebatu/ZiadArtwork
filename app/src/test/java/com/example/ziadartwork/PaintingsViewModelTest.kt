package com.example.ziadartwork

import FakeNetworkPaintingRepository
import androidx.lifecycle.SavedStateHandle
import com.example.ziadartwork.di.AppDispatchers
import com.example.ziadartwork.ui.viewmodels.MainActivityViewModel
import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.rules.TestDispatcherRule
import com.example.ziadartwork.ui.viewmodels.Result
import com.example.ziadartwork.domain.usecases.PaintingsUseCases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PaintingsViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher2 = AppDispatchers(
        IO = StandardTestDispatcher()
    )

    private lateinit var viewModel: MainActivityViewModel
    private val paintingsUseCases = PaintingsUseCases(FakeNetworkPaintingRepository())

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        viewModel = MainActivityViewModel(
            paintingsUseCases,
            testDispatcher2,
            savedStateHandle = SavedStateHandle()
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun PaintingsViewModel_getPaintings_verifyPaintingsUiStateSuccess() {
        runTest {

//            testDispatcher.testDispatcher.scheduler.advanceUntilIdle()

            val data = paintingsUseCases.getAllPaintings()

            val value = viewModel.paintingsState.value
            val s = ""

            assertEquals(
                Result.Success(emptyList<Painting>()), viewModel.paintingsState.value
            )
        }
    }


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}