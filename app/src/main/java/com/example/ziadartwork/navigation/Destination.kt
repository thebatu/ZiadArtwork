package com.example.ziadartwork.navigation

sealed class Destination(val route: String) {
    object MainDestination : Destination("main_screen")
    object DetailDestination : Destination("detail_screen")
    object ShoppingCartDestination: Destination("cart")

    fun withArgs(vararg args: String): String {
        var t = buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
        return t
    }
}
