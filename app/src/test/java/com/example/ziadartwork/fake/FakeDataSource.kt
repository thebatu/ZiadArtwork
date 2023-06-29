package com.example.ziadartwork.fake

import com.example.ziadartwork.ui.Result
import com.example.ziadartwork.data.model.Painting

object FakeDataSource {
    val paintingsMock = Result.Success(
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

