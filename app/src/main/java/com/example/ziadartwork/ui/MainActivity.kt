package com.example.ziadartwork.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.example.ziadartwork.theme.ZiadArtworkTheme
import com.example.ziadartwork.util.NetworkMonitor
import com.example.ziadartwork.util.rememberWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val windowSize = rememberWindowSizeClass()
            ZiadArtworkTheme {

                val zAppState = rememberZAppState(
                    networkMonitor = networkMonitor,
                )

                ZMainApp(
                    zAppState = zAppState,
                    windowSize = windowSize
                )
            }
        }
    }
}

