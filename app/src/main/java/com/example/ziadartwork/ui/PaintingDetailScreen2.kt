package com.example.ziadartwork.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.AbsoluteAlignment.TopLeft
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.unit.IntSize
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


@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun PaintingDetailScreen2(id: String) {
    val TAG = "PaintingDetailScreen"
    val viewModel: MainActivityViewModel = hiltViewModel()
    var zoomableImgVisible by remember { mutableStateOf(false) }

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
                    zoomableImgVisible = true
                }
        )
    }

    AnimatedVisibility(
        visible = zoomableImgVisible,
        enter = expandIn(
            // Overwrites the default spring animation with tween
            animationSpec = tween(200, easing = LinearOutSlowInEasing),
            // Overwrites the corner of the content that is first revealed
            expandFrom = Alignment.BottomEnd
        ) {
            // Overwrites the initial size to 50 pixels by 50 pixels
            IntSize(50, 50)
        },
        exit = shrinkOut(
            animationSpec = tween(200, easing = FastOutSlowInEasing),
            shrinkTowards = Alignment.BottomEnd,
        ) { fullSize ->
            Log.d(TAG, "PaintingDetailScreen2: dfdf")
            // Overwrites the target size of the shrinking animation.
            IntSize(fullSize.width / 10, fullSize.height / 5)
        }
    ) {

        Box(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
                .padding(16.dp)
                .clickable { zoomableImgVisible = false }
        ) {
            BackHandler {
                zoomableImgVisible = false
            }
            ZoomableImage2(model = painting!!.url)
        }

    }


}


@Composable
fun ZoomableImage2(model: String) {
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
