@file:OptIn(ExperimentalAnimationApi::class)

package com.example.ziadartwork.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.ziadartwork.util.WindowSize
import com.example.ziadartwork.ui.painting_detail.PaintingDetailSetup
import com.example.ziadartwork.ui.paintings.PaintingsHomeScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun CompatUI(
    windowSize: WindowSize,
) {
    val navController = rememberAnimatedNavController()

    Scaffold() { innerPadding ->
        AnimatedNavHost(
            navController = navController,
            startDestination = Destination.MainDestination.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = Destination.MainDestination.route) {
                PaintingsHomeScreen(
                    onPaintingSelected = { PaintingID ->
                        navController.navigate(PaintingID) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                )
            }
            composable(
                route = Destination.DetailDestination.route + "/{id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                ),
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                PaintingDetailSetup(paintingId = id.orEmpty(), navController)
//                MyUI()
            }
        }
    }
}
