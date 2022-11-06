package com.example.ziadartwork.model

import com.google.firebase.firestore.DocumentId

data class Painting(
    @DocumentId
    val id: String = "",
    val description: String = "",
    val isSold: Boolean = false,
    val name: String = "",
    val url: String = "",
    val price: Int = 0,

    )
