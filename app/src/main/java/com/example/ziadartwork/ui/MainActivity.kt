package com.example.ziadartwork.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ziadartwork.util.rememberWindowSizeClass
import com.example.ziadartwork.theme.ZiadArtworkTheme
import com.example.ziadartwork.navigation.CompatUI
import dagger.hilt.android.AndroidEntryPoint

    
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // Switch to AppTheme for displaying the activity
//        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        setContent {
            val windowSize = rememberWindowSizeClass()
            ZiadArtworkTheme {
                CompatUI(windowSize = windowSize)
            }
        }
    }
}

