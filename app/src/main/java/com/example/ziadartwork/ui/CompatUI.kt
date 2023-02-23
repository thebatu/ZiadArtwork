package com.example.ziadartwork.ui

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
import com.example.jetnews.navigation.Destination

@Composable
fun CompatUI(
    windowSize: WindowSize
) {
    val navController = rememberNavController()

    Scaffold() { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.MainDestination.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Destination.MainDestination.route) {
                PaintingsHomeScreen(
                    onPaintingSelected = { PaintingID ->
                        navController.navigate(PaintingID) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
            composable(
                route = Destination.DetailDestination.route + "/{id}",
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                PaintingDetailScreen(id = id.orEmpty())
            }
        }
    }

}

