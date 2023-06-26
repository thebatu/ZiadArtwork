package com.example.ziadartwork.ui.ui.composables

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ziadartwork.R
import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.ui.viewmodels.CartViewModel
import com.example.ziadartwork.ui.viewmodels.MainActivityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


@Preview(showBackground = true)
@Composable
fun PaintingDetailScreenPreview() {
//PaintingDetailScreen2("111")
}

@Composable
fun PaintingDetailSetup(
    id: String,
    navController: NavHostController,
) {

    val paintingViewModel: MainActivityViewModel = hiltViewModel()
    val shoppingCartViewModel: CartViewModel = hiltViewModel()
    var painting by remember(id) {
        mutableStateOf<Painting?>(null)
    }

    LaunchedEffect(id) {
        painting = paintingViewModel.getPainting(id)
    }

    PaintingDetailScreen(painting, navController, shoppingCartViewModel)
}

// TODO method too long brake it down into smaller chunks.
@Composable
fun PaintingDetailScreen(
    painting: Painting?,
    navController: NavHostController,
    shoppingCartViewModel: CartViewModel
) {
    val scope = rememberCoroutineScope()
    var isCartClicked by remember { mutableStateOf(false) }

    var paintingState by remember {
        mutableStateOf(PaintingState(ImageSize.Small, ImageZoom.NotZoomed))
    }

    val zoomInPainting: () -> Unit = {
        paintingState = paintingState.copy(zoom = ImageZoom.Zoomed)
    }

    val zoomOutPainting: () -> Unit = {
        paintingState = paintingState.copy(zoom = ImageZoom.NotZoomed)
    }

    val shrinkPainting: () -> Unit = {
        paintingState = paintingState.copy(size = ImageSize.Small, zoom = ImageZoom.NotZoomed)
    }


    val popBackStack: () -> Unit = {
        when (paintingState) {
            PaintingState(ImageSize.Small, ImageZoom.Zoomed) -> zoomOutPainting()
            PaintingState(ImageSize.Large, ImageZoom.NotZoomed) -> shrinkPainting()
            PaintingState(ImageSize.Large, ImageZoom.Zoomed) -> shrinkPainting()
            PaintingState(ImageSize.Small, ImageZoom.NotZoomed) -> navController.popBackStack()
        }
    }

    val enlargePainting: () -> Unit = {
        paintingState = paintingState.copy(size = ImageSize.Large)
    }

    val togglePaintingSize: () -> Unit = {
        when (paintingState.size) {
            ImageSize.Small -> enlargePainting()
            ImageSize.Large -> shrinkPainting()
        }
    }

    //Cart related variables and anim
    val cartScale: Float by animateFloatAsState(
        if (isCartClicked) 1.6f else 1f,
        animationSpec = tween(durationMillis = 400),
        label = "cart Scale"
    )
    //TODO remove forceRecomposition for animation
    val forceRecompose = rememberUpdatedState(cartScale)

    val sizeTransition = updateTransition(paintingState.size, label = "sizeTransition")
    val paintingSize by sizeTransition.animateDp(label = "painting Size Transition") {
        when (it) {
            ImageSize.Small -> 400.dp
            ImageSize.Large -> 600.dp
        }
    }

    val paintingOffset by sizeTransition.animateOffset(transitionSpec = {
        if (this.initialState == ImageSize.Small) {
            tween(400) // launch duration
        } else {
            tween(600) // land duration
        }
    }, label = "painting offset") { size ->
        if (size == ImageSize.Large) {
            //TODO this will not work on all screen devices, initial offset should be set from the outside
            Offset(0f, 100f)
        } else {
            Offset(0f, 0f)
        }
    }


    var itemCount = 2

    if (painting != null) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ZoomablePaintingImg(
                imageUrl = painting.url,
                modifier = Modifier
                    .offset(paintingOffset.x.dp, paintingOffset.y.dp)
                    .size(size = paintingSize)
                    .padding(8.dp),
                resizeImg = togglePaintingSize,
                goBack = popBackStack,
                paintingLarge = zoomInPainting,
                paintingSmall = zoomOutPainting,
            )

            AnimatedVisibility(
                visible = paintingState.zoom == ImageZoom.NotZoomed && paintingState.size == ImageSize.Small,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = painting.name,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )

                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        //TODO add a size to the painting model and replace static var
                        text = "65 x 56 cm",

                        )

                    Spacer(Modifier.weight(1f))

                    AnimatedVisibility(
                        visible = paintingState.zoom == ImageZoom.NotZoomed && paintingState.size == ImageSize.Small,
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally()
                    ) {
                        Box {
                            Image(
                                modifier = Modifier
                                    .scale(forceRecompose.value)
                                    .padding(8.dp)
                                    .clickable(
                                        onClick = {
                                            isCartClicked = true
                                            shoppingCartViewModel.addPaintingToCart(paintingId = painting.id)
                                            scope.launch {
                                                Log.d("CartClick", "Coroutine launched")
                                                delay(400)
                                                isCartClicked = false
                                                Log.d("CartClick", "Coroutine ended")
                                            }
                                        }
                                    ),
                                painter = painterResource(R.drawable.ic_baseline_add_shopping_cart_24),
                                contentDescription = null,
                            )
                            if (itemCount > 0) {
                                Text(
                                    text = itemCount.toString(),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .background(Color.Red, CircleShape)
                                        .padding(horizontal = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = paintingState.zoom == ImageZoom.NotZoomed && paintingState.size == ImageSize.Small,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
            ) {
                Text(modifier = Modifier.padding(8.dp), text = painting.description)
            }
        }
    }
}

@Composable
fun ZoomablePaintingImg(
    imageUrl: String,
    modifier: Modifier,
    resizeImg: () -> Unit,
    goBack: () -> Unit,
    paintingLarge: () -> Unit,
    paintingSmall: () -> Unit
) {
    var imageRotationAngle by remember { mutableStateOf(0f) }
    var imageZoomLevel by remember { mutableStateOf(1f) }
    var imageOffsetX by remember { mutableStateOf(0f) }
    var imageOffsetY by remember { mutableStateOf(0f) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp.value
    val screenHeight = configuration.screenHeightDp.dp.value

    fun resetImgAttributes() {
        imageZoomLevel = 1f
        imageOffsetX = 0f
        imageOffsetY = 0f
        imageRotationAngle = 0f
        paintingSmall()
    }

    BackHandler(enabled = true) {
        if (isImageZoomed(imageZoomLevel, imageOffsetX, imageOffsetY, imageRotationAngle)) {
            resetImgAttributes()
        } else {
            goBack()
        }
    }

    AsyncImage(
        imageUrl,
        contentDescription = "Painting Detail",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .offset { IntOffset(imageOffsetX.roundToInt(), imageOffsetY.roundToInt()) }
            .graphicsLayer(
                scaleX = imageZoomLevel,
                scaleY = imageZoomLevel,
                rotationZ = imageRotationAngle,
            )
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { _, pan, gestureZoom, _ ->
                        imageZoomLevel = (imageZoomLevel * gestureZoom).coerceIn(1F..8F)
                        if (imageZoomLevel > 1) {
                            paintingLarge()
                            val x = (pan.x * imageZoomLevel)
                            val y = (pan.y * imageZoomLevel)
                            val angleRad = imageRotationAngle * PI / 180.0

                            imageOffsetX =
                                (imageOffsetX + (x * cos(angleRad) - y * sin(angleRad)).toFloat()).coerceIn(
                                    -(screenWidth * imageZoomLevel)..(screenWidth * imageZoomLevel),
                                )
                            imageOffsetY =
                                (imageOffsetY + (x * sin(angleRad) + y * cos(angleRad)).toFloat()).coerceIn(
                                    -(screenHeight * imageZoomLevel)..(screenHeight * imageZoomLevel),
                                )
                        } else {
                            imageOffsetX = 0F
                            imageOffsetY = 0F
                        }
                    },
                )
            }
            .clickable(
                onClick = {
                    if (isImageZoomed(
                            imageZoomLevel,
                            imageOffsetX,
                            imageOffsetY,
                            imageRotationAngle
                        )
                    ) {
                        resetImgAttributes()
                    } else {
                        resizeImg()
                    }
                },
            )

    )
}

fun isImageZoomed(zoom: Float, offsetX: Float, offsetY: Float, angle: Float): Boolean {
    return (zoom != 1f || offsetX != 0f || offsetY != 0f || angle != 0f)
}

data class PaintingState(val size: ImageSize, val zoom: ImageZoom)
enum class ImageSize { Small, Large }
enum class ImageZoom { NotZoomed, Zoomed }