package com.example.ziadartwork.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.ziadartwork.R
import com.example.ziadartwork.model.Painting
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PaintingDetailScreen(id: String) {
    val TAG = "PaintingDetailScreen"
    val viewModel: MainActivityViewModel = hiltViewModel()
    var openDialog by remember { mutableStateOf(false) }

    var painting by remember(id) {
        mutableStateOf<Painting?>(null)
    }

    LaunchedEffect(id) {
        Log.d(TAG, "PaintingDetailScreen: ")
        painting = viewModel.getPainting(id)
    }

    if (painting != null) {
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
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxHeight(0.4f)
                .fillMaxWidth()
                .padding(16.dp)
                .border(BorderStroke(1.dp, Color.White))
                .clickable {
                    openDialog = true
                }
        )
    }

    if (openDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clickable { openDialog = false }
            ) {
            ZoomableImage(model = painting!!.url)
        }

    }
}


@Composable
fun ZoomableImage(model: String) {
    val angle by remember { mutableStateOf(0f) }
    var zoom by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp.value
    val screenHeight = configuration.screenHeightDp.dp.value

    AsyncImage(
        model,
        contentDescription = "Painting Detail",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .border(BorderStroke(1.dp, Color.White))
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .graphicsLayer(
                scaleX = zoom,
                scaleY = zoom,
                rotationZ = angle
            )
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { _, pan, gestureZoom, _ ->
                        zoom = (zoom * gestureZoom).coerceIn(1F..8F)
                        if (zoom > 1) {
                            val x = (pan.x * zoom)
                            val y = (pan.y * zoom)
                            val angleRad = angle * PI / 180.0

                            offsetX =
                                (offsetX + (x * cos(angleRad) - y * sin(angleRad)).toFloat()).coerceIn(
                                    -(screenWidth * zoom)..(screenWidth * zoom)
                                )
                            offsetY =
                                (offsetY + (x * sin(angleRad) + y * cos(angleRad)).toFloat()).coerceIn(
                                    -(screenHeight * zoom)..(screenHeight * zoom)
                                )
                        } else {
                            offsetX = 0F
                            offsetY = 0F
                        }
                    }
                )
            }
            .fillMaxSize()
    )
}


//@Composable
//fun ZoomableImage(url: String) {
//    val asyncImage: AsyncImagePainter = rememberAsyncImagePainter(url)
//    Image(
//        painter = asyncImage,
//        contentDescription = null,
//        contentScale = ContentScale.Fit,
//        modifier = Modifier.fillMaxSize()
//
//
//    )
//}





