package com.example.ziadartwork.fake

import com.example.ziadartwork.Response
import com.example.ziadartwork.model.Painting
import kotlinx.coroutines.flow.flowOf

object FakeDataSource {
    val paintingsMock = Response.Success(
        listOf(
            Painting(
                id = "1",
                description = "mock painting",
                isSold = false,
                name = "super painting",
                url = "https://google.com"
            )
        )
    )
}

