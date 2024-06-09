package com.example.ziadartwork.navigation

const val HOME_ROUTE = "home"
const val DETAIL_SCREEN = "detail_screen"
const val SHOPPING = "shopping"
const val PROFILE = "profile"
const val SPLASH_SCREEN = "splash_screen"

sealed class TopLevelDestination(val route: String) {
    object MainDestination : TopLevelDestination(HOME_ROUTE)
    object SplashDestination : TopLevelDestination(SPLASH_SCREEN)
    object DetailDestination : TopLevelDestination(DETAIL_SCREEN)
    object ShoppingCartDestination: TopLevelDestination(SHOPPING)
    object UserSettings: TopLevelDestination(PROFILE)

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