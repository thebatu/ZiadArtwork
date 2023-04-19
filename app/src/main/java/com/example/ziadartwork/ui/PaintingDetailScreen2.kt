package com.example.ziadartwork.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ziadartwork.model.Painting
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

lateinit var imgModel2: String

@Preview()
@Composable
fun PaintingDetailScreenPreview2() {
    ZoomablePaintingImg("111")
}

@Composable
fun PaintingDetailSetup2(
    id: String,
    navController: NavHostController,
) {
    val viewModel: MainActivityViewModel = hiltViewModel()
    var painting2 by remember(id) {
        mutableStateOf<Painting?>(null)
    }

    LaunchedEffect(id) {
        val TAG = "PaintingDetailScreen"
        Log.d(TAG, "PaintingDetailScreen: ")
        painting2 = viewModel.getPainting(id)
        imgModel2 = painting2?.url ?: ""
    }
    PaintingDetailScreen2(painting2, navController)
}

private enum class PaintingState {
    Small, Large
}

@Composable
fun PaintingDetailScreen2(
    painting: Painting?,
    navController: NavHostController,
) {
    var paintingState by remember {
        mutableStateOf(PaintingState.Small)
    }
    val transition = updateTransition(targetState = paintingState, label = "transition")

    val toggleLargeImgVisibility: () -> Unit = {
        if (paintingState == PaintingState.Small) {
            paintingState = PaintingState.Large
        } else {
            paintingState = PaintingState.Small
        }
    }

    val popBackStack: () -> Unit = {
        navController.popBackStack()
    }


    val paintingSize by transition.animateDp(label = "painting Size Transition") {
        when (it) {
            PaintingState.Small -> 350.dp
            PaintingState.Large -> 500.dp
        }
    }

    val paintingOffset by transition.animateOffset(transitionSpec = {
        if (this.initialState == PaintingState.Small) {
            tween(400) // launch duration
        } else {
            tween(600) // land duration
        }
    }, label = "painting offset") { animated ->
        if (animated == PaintingState.Large) {
            Offset(0f, 100f)
        } else {
            Offset(0f, 0f)
        }
    }

    if (painting != null) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ZoomablePaintingImg2(
                model = painting.url,
                modifier = Modifier
                    .padding(16.dp)
                    .offset(paintingOffset.x.dp, paintingOffset.y.dp)
                    .size(size = paintingSize),
                resizeImg = toggleLargeImgVisibility,
                goBack = popBackStack,
            )


        }
    }
}

@Composable
fun ZoomablePaintingImg2(
    model: String,
    modifier: Modifier,
    resizeImg: () -> Unit,
    goBack: () -> Unit
) {
    var angle by remember { mutableStateOf(0f) }
    var zoom by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp.value
    val screenHeight = configuration.screenHeightDp.dp.value

    fun resetImgZoom() {
        zoom = 1f
        offsetX = 0f
        offsetY = 0f
        angle = 0f
    }

    BackHandler(enabled = true) {
        if (zoomedImage(zoom, offsetX, offsetY, angle)) {
            resetImgZoom()
        } else {
            goBack()
        }
    }

    AsyncImage(
        model,
        contentDescription = "Painting Detail",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .graphicsLayer(
                scaleX = zoom,
                scaleY = zoom,
                rotationZ = angle,
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
                                    -(screenWidth * zoom)..(screenWidth * zoom),
                                )
                            offsetY =
                                (offsetY + (x * sin(angleRad) + y * cos(angleRad)).toFloat()).coerceIn(
                                    -(screenHeight * zoom)..(screenHeight * zoom),
                                )
                        } else {
                            offsetX = 0F
                            offsetY = 0F
                        }
                    },
                )
            }
            .clickable(
                onClick = {
                    if (zoomedImage(zoom, offsetX, offsetY, angle)) {
                        resetImgZoom()
                    } else {
                        resizeImg()
                    }
                },
            ),
    )
}

fun zoomedImage(zoom: Float, offsetX: Float, offsetY: Float, angle: Float): Boolean {
    return (zoom != 1f || offsetX != 0f || offsetY != 0f || angle != 0f)
}
