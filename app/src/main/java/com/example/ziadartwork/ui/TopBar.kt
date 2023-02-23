package com.example.ziadartwork.ui

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.ziadartwork.R

@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name)
            )
        }
    )
}