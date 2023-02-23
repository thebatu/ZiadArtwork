package com.example.jetnews.navigation

sealed class Destination(val route: String) {
    object MainDestination : Destination("main_screen")
    object DetailDestination : Destination("detail_screen")

    fun withArgs(vararg args: String): String {
        var t =  buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
        return t
    }
}
