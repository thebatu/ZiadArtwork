package com.example.ziadartwork.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ziadartwork.R
import com.example.ziadartwork.model.Painting
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


//18807
lateinit var imgModel2: String

@Preview(showBackground = true)
@Composable
fun PaintingDetailScreenPreview() {
//    PaintingDetailScreen2("111")
}

@Composable
fun PaintingDetailSetup(
    id: String,
    navController: NavHostController,
) {
    val viewModel: MainActivityViewModel = hiltViewModel()
    var painting by remember(id) {
        mutableStateOf<Painting?>(null)
    }

    LaunchedEffect(id) {
        val TAG = "PaintingDetailScreen"
        Log.d(TAG, "PaintingDetailScreen:  ")
        painting = viewModel.getPainting(id)
        imgModel2 = painting?.url ?: ""
    }
    PaintingDetailScreen(painting, navController)
}

private enum class PaintingSize {
    Small, Large
}

private enum class PaintingZoom {
    Small, Large
}

@Composable
fun PaintingDetailScreen(
    painting: Painting?,
    navController: NavHostController,
) {
    var paintingState by remember {
        mutableStateOf(PaintingSize.Small)
    }

    var paintingZoom by remember {
        mutableStateOf(PaintingZoom.Small)
    }

    val paintingLarge: () -> Unit = {
        paintingZoom = PaintingZoom.Large
    }

    val paintingSmall: () -> Unit = {
        paintingZoom = PaintingZoom.Small
    }

    val popBackStack: () -> Unit = {
        if (paintingState == PaintingSize.Large) {
            paintingState = PaintingSize.Small
            paintingSmall()
        } else {
            navController.popBackStack()
        }
    }

    val toggleLargeImgVisibility: () -> Unit = {
        if (paintingState == PaintingSize.Small) {
            paintingState = PaintingSize.Large
            paintingLarge()
        } else {
            paintingState = PaintingSize.Small
            paintingSmall()
        }
    }

    val transition = updateTransition(targetState = paintingState, label = "transition")

    val paintingSize by transition.animateDp(label = "painting Size Transition") {
        when (it) {
            PaintingSize.Small -> 400.dp
            PaintingSize.Large -> 600.dp
        }
    }

    val paintingOffset by transition.animateOffset(transitionSpec = {
        if (this.initialState == PaintingSize.Small) {
            tween(400) // launch duration
        } else {
            tween(600) // land duration
        }
    }, label = "painting offset") { animated ->
        if (animated == PaintingSize.Large) {
            Offset(0f, 100f)
        } else {
            Offset(0f, 0f)
        }
    }

    if (painting != null) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ZoomablePaintingImg(
                model = painting.url,
                modifier = Modifier
                    .offset(paintingOffset.x.dp, paintingOffset.y.dp)
                    .size(size = paintingSize)
                    .padding(8.dp),
                resizeImg = toggleLargeImgVisibility,
                goBack = popBackStack,
                paintingLarge = paintingLarge,
                paintingSmall = paintingSmall,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End

            ) {
                AnimatedVisibility(
                    visible = paintingZoom == PaintingZoom.Small && paintingState == PaintingSize.Small,
                    enter = fadeIn() + expandHorizontally(),
                    exit = fadeOut() + shrinkHorizontally()
                ) {

                    Image(
                        painter = painterResource(R.drawable.ic_baseline_add_shopping_cart_24),
                        contentDescription = null,
                    )
                }
            }

        }
    }
}

@Composable
fun ZoomablePaintingImg(
    model: String,
    modifier: Modifier,
    resizeImg: () -> Unit,
    goBack: () -> Unit,
    paintingLarge: () -> Unit,
    paintingSmall: () -> Unit
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
        paintingSmall()
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
                            paintingLarge()
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
            )

    )
}

fun zoomedImage(zoom: Float, offsetX: Float, offsetY: Float, angle: Float): Boolean {
    return (zoom != 1f || offsetX != 0f || offsetY != 0f || angle != 0f)
}
