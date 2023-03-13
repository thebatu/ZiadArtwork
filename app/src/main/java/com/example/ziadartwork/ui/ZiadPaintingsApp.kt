package com.example.ziadartwork.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.jetnews.navigation.Destination
import com.example.ziadartwork.R
import com.example.ziadartwork.Result


//@OptIn(ExperimentalLifecycleComposeApi::class)
//@Composable
//fun ZiadPaintingsApp(
//    navigate: NavController,
//    modifier: Modifier = Modifier,
//) {
//    Scaffold(
//        modifier = modifier.fillMaxSize(),
//        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) }
//    ) {
//        Surface(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(it),
//            color = MaterialTheme.colors.background
//        ) {
//            val viewModel: MainActivityViewModel = hiltViewModel()
//            val homeScreenState by remember(viewModel) {
//                viewModel.fetchPaintings
//            }.collectAsStateWithLifecycle(Result.Loading)
//
////            PaintingsHomeScreen(
////                paintingsScreenState = homeScreenState,
////                retryAction = { },
////                onPaintingSelected = { navController.navigate("detail/${it.name}") })
////            )
//        }
//    }
//
//
//}


//fun navigateTo(navigate: NavController, id: String) {
//    navigate.navigate(Destination.DetailDestination.withArgs(id))
//}