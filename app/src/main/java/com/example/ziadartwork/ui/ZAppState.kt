package com.example.ziadartwork.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ziadartwork.navigation.TopLevelDestination
import com.example.ziadartwork.util.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberZAppState(
    networkMonitor: NetworkMonitor,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): ZAppState {

    return remember(
        navController,
        coroutineScope,
    ) {
        ZAppState(
            navController = navController,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
        )
    }
}


class ZAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
    ) {
    @Composable
    fun currentDestinationRoute(): String? {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination?.route
    }

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination


    val showNavigationBar: Boolean
        @Composable get() = currentDestinationRoute() in listOf(
            TopLevelDestination.MainDestination.route,
            TopLevelDestination.ShoppingCartDestination.route,
            TopLevelDestination.UserSettings.route,
        )

    val isOffline = networkMonitor.isOnline.map(Boolean::not).stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
        this?.hierarchy?.any {
            it.route?.contains(destination.route, true) ?: false
        } ?: false
}