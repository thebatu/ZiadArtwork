package com.example.ziadartwork.UI

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.example.ziadartwork.R


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ZiadPaintingsApp(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colors.background
        ) {
            val viewModel: MainActivityViewModel = hiltViewModel()
            val homeScreenState by viewModel.paintingsState.collectAsStateWithLifecycle()
            val homeScreenState2 by remember(viewModel) {viewModel.paintingsState}.collectAsStateWithLifecycle()
            PaintingsHomeScreen(
                paintingsScreenState = homeScreenState2,
                retryAction = {  }
            )
        }
    }
}