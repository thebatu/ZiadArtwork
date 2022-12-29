package com.example.ziadartwork

import FakeNetworkPaintingRepository
import com.example.ziadartwork.UI.MainActivityViewModel
import com.example.ziadartwork.model.Painting
import com.example.ziadartwork.rules.TestDispatcherRule
import com.example.ziadartwork.usecases.GetPaintingsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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
    private val getPaintingsUseCase = GetPaintingsUseCase(FakeNetworkPaintingRepository())


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        viewModel = MainActivityViewModel(
            getPaintingsUseCase,
            testDispatcher2
        )
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun PaintingsViewModel_getPaintings_verifyPaintingsUiStateSuccess() {
        runTest {

//            testDispatcher.testDispatcher.scheduler.advanceUntilIdle()

            val data = getPaintingsUseCase.invoke()

            val value = viewModel.paintingsState.value
            val s = ""

            assertEquals(
                Response.Success(emptyList<Painting>()), viewModel.paintingsState.value
            )
        }
    }


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}