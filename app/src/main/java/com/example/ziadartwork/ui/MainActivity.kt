package com.example.ziadartwork.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ziadartwork.R
import com.example.ziadartwork.ui.theme.ZiadArtworkTheme
import dagger.hilt.android.AndroidEntryPoint

    
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // Switch to AppTheme for displaying the activity
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        setContent {
            val windowSize = rememberWindowSizeClass()
            ZiadArtworkTheme {
                CompatUI(windowSize = windowSize)
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    val windowSize = rememberWindowSizeClass()
//    ZiadArtworkTheme {
//        ZiadApp(windowSize)
//    }
//}
//
