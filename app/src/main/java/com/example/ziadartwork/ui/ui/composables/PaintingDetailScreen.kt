package com.example.ziadartwork.ui.ui.composables

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.ui.viewmodels.CartViewModel
import com.example.ziadartwork.ui.viewmodels.MainActivityViewModel
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

@Composable
fun PaintingDetailScreen(
    painting: Painting?,
    navController: NavHostController,
    shoppingCartViewModel: CartViewModel
) {
    var paintingSizeState by remember {
        mutableStateOf(PaintingSize.Small)
    }

    var paintingZoomState by remember {
        mutableStateOf(PaintingZoom.NotZoomed)
    }

    val zoomInPainting: () -> Unit = {
        paintingZoomState = PaintingZoom.Zoomed
    }

    val zoomOutPainting: () -> Unit = {
        paintingZoomState = PaintingZoom.NotZoomed
    }

    val popBackStack: () -> Unit = {
        if (paintingSizeState == PaintingSize.Large) {
            paintingSizeState = PaintingSize.Small
            zoomOutPainting()
        } else {
            navController.popBackStack()
        }
    }

    val togglePaintingSize: () -> Unit = {
        if (paintingSizeState == PaintingSize.Small) {
            paintingSizeState = PaintingSize.Large
            zoomInPainting()
        } else {
            paintingSizeState = PaintingSize.Small
            zoomOutPainting()
        }
    }

    //Cart related variables and anim
    var isCartClicked by remember { mutableStateOf(false) }
    val cartScale: Float by animateFloatAsState(
        if (isCartClicked) 1.6f else 1f,
        animationSpec = tween(durationMillis = 400),
        label = "cart Scale"
    )

    val transition = updateTransition(targetState = paintingSizeState, label = "transition")
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
                visible = paintingZoomState == PaintingZoom.NotZoomed && paintingSizeState == PaintingSize.Small,
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
                        visible = paintingZoomState == PaintingZoom.NotZoomed && paintingSizeState == PaintingSize.Small,
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally()
                    ) {
                        Image(
                            modifier = Modifier
                                .scale(cartScale)
                                .padding(8.dp)
                                .clickable(
                                    onClick = {
                                        isCartClicked = true
                                        Log.d("CartClick", "Cart state: $isCartClicked")
                                        shoppingCartViewModel.addPaintingToCart(paintingId = painting.id)
                                        isCartClicked = false
                                    }
                                ),
                            painter = painterResource(R.drawable.ic_baseline_add_shopping_cart_24),
                            contentDescription = null,
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = paintingZoomState == PaintingZoom.NotZoomed && paintingSizeState == PaintingSize.Small,
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
                    if (isImageZoomed(imageZoomLevel, imageOffsetX, imageOffsetY, imageRotationAngle)) {
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

private enum class PaintingSize {
    Small, Large
}

private enum class PaintingZoom {
    NotZoomed, Zoomed
}