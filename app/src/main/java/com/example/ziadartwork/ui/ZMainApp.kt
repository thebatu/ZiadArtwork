package com.example.ziadartwork.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.ziadartwork.R
import com.example.ziadartwork.navigation.CompatUI
import com.example.ziadartwork.navigation.TopLevelDestination
import com.example.ziadartwork.util.WindowSize


@Composable
fun ZMainApp(
    zAppState: ZAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    windowSize: WindowSize,

    ) {

    val snackbarHostState = remember { SnackbarHostState() }

    val shouldShowBackground =
        zAppState.currentDestination?.route == TopLevelDestination.MainDestination.route

    val isOffline by zAppState.isOffline.collectAsStateWithLifecycle()
    val notConnectedMessage = stringResource(R.string.not_connected)

    LaunchedEffect(isOffline) {
        if (isOffline) {
            snackbarHostState.showSnackbar(
                message = notConnectedMessage,
                duration = SnackbarDuration.Indefinite,
            )
        }
    }

    ZMainApp(
        appState = zAppState,
        snackbarHostState = snackbarHostState,
        windowSize = windowSize,

        )
}

@Composable
internal fun ZMainApp(
    snackbarHostState: SnackbarHostState,
    appState: ZAppState,
    windowSize: WindowSize,
) {
    val currentDestination = appState.currentDestination

    NavigationSuiteScaffold(
        modifier = Modifier.safeContentPadding(),
        navigationSuiteItems = {
            AppDestinations.entries.forEachIndexed { _, screen ->

                val selected = currentDestination.isTopLevelDestinationInHierarchy(screen)

                item(
                    onClick = {
                        appState.navController.navigate(screen.name) {
                            popUpTo(appState.navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.name
                        )
                    },
                    label = {
                        Text(text = screen.name)
                    },
                    selected = selected

                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Your content goes here, for example:
            CompatUI(
                navController = appState.navController,
                insets = Modifier
            )
        }
    }
}

enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
) {
    HOME(R.string.home, Icons.Default.Home, R.string.home),
    SHOPPING(R.string.shopping, Icons.Default.ShoppingCart, R.string.shopping),
    PROFILE(R.string.profile, Icons.Default.AccountBox, R.string.profile),
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: AppDestinations) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false