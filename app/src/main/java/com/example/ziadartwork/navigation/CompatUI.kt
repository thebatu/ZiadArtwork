@file:OptIn(ExperimentalAnimationApi::class)

package com.example.ziadartwork.navigation

import ShoppingCartScreen
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ziadartwork.ui.componants.TopBar
import com.example.ziadartwork.util.WindowSize
import com.example.ziadartwork.ui.painting_detail.PaintingDetailSetup
import com.example.ziadartwork.ui.paintings.PaintingsHomeScreen

@Composable
fun CompatUI(
    windowSize: WindowSize,
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopBar {
                navController.navigate(Destination.ShoppingCartDestination.route) {
                    launchSingleTop = true
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.MainDestination.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = Destination.MainDestination.route) {
                PaintingsHomeScreen(
                    onPaintingSelected = { paintingID ->
                        navController.navigate(paintingID) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            // Restore state when re-selecting a previously selected item
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

            composable(route = Destination.ShoppingCartDestination.route) {
                ShoppingCartScreen()
            }


        }
    }
}
