package com.example.ziadartwork.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.ziadartwork.R
import com.example.ziadartwork.model.Painting


@Composable
fun PaintingDetailScreen(id: String) {
    val TAG = "PaintingDetailScreen"
    val viewModel: MainActivityViewModel = hiltViewModel()
    
    val alertDialog  = { AlertDialog(onDismissRequest = { }, title = "test") {
        
    } }


    var painting by remember(id) {
        mutableStateOf<Painting?>(null)
    }

    LaunchedEffect(id) {
        Log.d(TAG, "PaintingDetailScreen: ")
        painting = viewModel.getPainting(id)
    }


    if (painting != null) {
        Box(modifier = Modifier
            .clickable {


            }
        )
        {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(painting!!.url)
                    .crossfade(true)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .setHeader("Cache-Control", "max-age=31536000")
                    .networkCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = "Painting detail image",
                placeholder = painterResource(R.drawable.ic_broken_image),
                error = painterResource(R.drawable.ic_broken_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight(0.4f)
                    .padding(16.dp)
                    .border(BorderStroke(1.dp, Color.White))
            )
        }
    }
}

@Composable
fun showPaintingPopup(): Unit {

}

