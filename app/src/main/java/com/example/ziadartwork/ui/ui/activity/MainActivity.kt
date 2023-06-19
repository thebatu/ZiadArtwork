package com.example.ziadartwork.ui.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ziadartwork.ui.rememberWindowSizeClass
import com.example.ziadartwork.theme.ZiadArtworkTheme
import com.example.ziadartwork.ui.ui.composables.CompatUI
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

