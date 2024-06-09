@file:OptIn(ExperimentalAnimationApi::class)

package com.example.ziadartwork.navigation

import ShoppingCartScreen
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ziadartwork.util.WindowSize
import com.example.ziadartwork.ui.painting_detail.PaintingDetailSetup
import com.example.ziadartwork.ui.paintings.PaintingsHomeScreen

@Composable
fun CompatUI(
    navController: NavHostController,
    insets: Modifier,
) {

    Scaffold() { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TopLevelDestination.MainDestination.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = TopLevelDestination.MainDestination.route) {
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
                route = TopLevelDestination.DetailDestination.route + "/{id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                ),
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                PaintingDetailSetup(
                    paintingId = id.orEmpty(),
                    navController,
                    modifier = insets.padding(innerPadding)
                )
//                MyUI()
            }

            composable(route = TopLevelDestination.ShoppingCartDestination.route) {
                ShoppingCartScreen()
            }


        }
    }
}
