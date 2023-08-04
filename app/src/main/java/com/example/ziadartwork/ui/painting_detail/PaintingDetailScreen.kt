package com.example.ziadartwork.ui.painting_detail

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ziadartwork.R
import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.ui.painting_detail.cart.CartViewModel
import com.example.ziadartwork.ui.paintings.MainActivityViewModel
import com.example.ziadartwork.ui.paintings.MainActivityViewModel.PaintingsUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Preview(showBackground = true)
@Composable
fun PaintingDetailScreenPreview() {}

@Composable
fun PaintingDetailSetup(
    paintingId: String,
    navController: NavHostController,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp.value
    val screenHeight = configuration.screenHeightDp.dp.value

    val shoppingCartViewModel: CartViewModel = hiltViewModel()
    val paintingViewModel: MainActivityViewModel = hiltViewModel()
    var painting by remember(paintingId) { mutableStateOf<Painting?>(null) }

    var paintingState by remember {
        mutableStateOf<PaintingsUiState<Painting?>>(PaintingsUiState.Loading)
    }

    LaunchedEffect(paintingId) {
        paintingState = paintingViewModel.getPainting(paintingId)
    }

    val cartCount by shoppingCartViewModel.getCurrentPaintingCount(paintingId).collectAsState()

    when (val state = paintingState) {
        is PaintingsUiState.Loading -> {
            // TODO display loading
        }
        is PaintingsUiState.Error -> {
            // TODO show a toast with the error msg
        }
        is PaintingsUiState.Success -> {
            PaintingDetailScreen(state.data, navController, shoppingCartViewModel, cartCount, screenHeight, screenWidth)
        }
    }
}

@Composable
fun PaintingDetailScreen(
    painting: Painting?,
    navController: NavHostController,
    shoppingCartViewModel: CartViewModel,
    cartCount: Int,
    screenHeight: Float,
    screenWidth: Float,
) {

    val density: Density = LocalDensity.current

    val scope = rememberCoroutineScope()
    var isCartClicked by remember { mutableStateOf(false) }

    var paintingState by remember {
        mutableStateOf(
            PaintingState(
                ImageSize.Small,
                ImageZoom.NotZoomed,
            )
        )
    }

    val zoomInPainting: () -> Unit = {
        paintingState = paintingState.copy(zoom = ImageZoom.Zoomed)
    }

    val zoomOutPainting: () -> Unit = {
        paintingState = paintingState.copy(zoom = ImageZoom.NotZoomed)
    }

    val shrinkPainting: () -> Unit = {
        paintingState = paintingState.copy(size = ImageSize.Small)
    }

    val enlargePainting: () -> Unit = {
        paintingState = paintingState.copy(size = ImageSize.Large)
    }

    val popBackStack: () -> Unit = {
        when (paintingState) {
            PaintingState(ImageSize.Small, ImageZoom.Zoomed) -> {
                zoomOutPainting()
            }
            PaintingState(ImageSize.Large, ImageZoom.NotZoomed) -> {
                shrinkPainting()
            }
            PaintingState(ImageSize.Large, ImageZoom.Zoomed) -> {
                shrinkPainting()
            }
            PaintingState(
                ImageSize.Small,
                ImageZoom.NotZoomed
            ) -> navController.popBackStack()

            PaintingState(ImageSize.Large, ImageZoom.NotZoomed) -> {
                shrinkPainting()
            }
        }
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

    val sizeTransition =
        updateTransition(targetState = paintingState.size, label = "sizeTransition")

    val paintingSize by sizeTransition.animateDp(label = "painting Size Transition") {
        when (it) {
            ImageSize.Small -> 400.dp
            ImageSize.Large -> {
                val largeSize = 600.dp
                if (screenWidth < largeSize.value) {
                    screenWidth.dp
                } else {
                    largeSize
                }
            }
        }
    }

    val paintingOffset by sizeTransition.animateOffset(transitionSpec = {
        if (this.initialState == ImageSize.Small) {
            tween(400) // launch duration
        } else {
            tween(600) // land duration
        }
    }, label = "painting offset") { targetState ->
        //TODO this will not work on all screen devices, initial offset should be set from the outside
        with(density) {
            val y = when (targetState) {
                ImageSize.Small -> 0f // at the top
                ImageSize.Large -> ((screenHeight - paintingSize.value) / 2f) // at the center
            }
            Offset(
                x = when (targetState) {
                    ImageSize.Small -> ((screenWidth - paintingSize.value) / 2f) // at the center
                    ImageSize.Large -> ((screenWidth - paintingSize.value) / 2f) // at the center
                },
                y = y
            )
        }
    }

    val onCartIconClick: () -> Unit = {
        isCartClicked = true
        scope.launch {
            painting?.let {
                shoppingCartViewModel.addPaintingToCart(paintingId = it.id)
                delay(200) //needed for the cart animation
                isCartClicked = false
            }
        }
    }

    painting?.let {
        PaintingDetailContent(
            painting = it,
            paintingSize = paintingSize,
            paintingOffset = paintingOffset,
            cartScale = cartScale,
            isCartClicked = isCartClicked,
            paintingState = paintingState,
            togglePaintingSize = togglePaintingSize,
            popBackStack = popBackStack,
            zoomInPainting = zoomInPainting,
            zoomOutPainting = zoomOutPainting,
            currentPaintingCartItemCount = cartCount,
            shoppingCartViewModel = shoppingCartViewModel,
            onCartIconClick = onCartIconClick,
            screenHeight = screenHeight,
            screenWidth = screenWidth
        )
    }
}

@Composable
fun PaintingDetailContent(
    painting: Painting,
    paintingSize: Dp,
    paintingOffset: Offset,
    cartScale: Float,
    isCartClicked: Boolean,
    paintingState: PaintingState,
    togglePaintingSize: () -> Unit,
    popBackStack: () -> Unit,
    zoomInPainting: () -> Unit,
    zoomOutPainting: () -> Unit,
    currentPaintingCartItemCount: Int,
    shoppingCartViewModel: CartViewModel,
    onCartIconClick: () -> Unit,
    screenHeight: Float,
    screenWidth: Float,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.offset(paintingOffset.x.dp, paintingOffset.y.dp)
        ) {
            ZoomablePaintingImg(
                imageUrl = painting.url,
                modifier = Modifier
                    .size(paintingSize)
                    .padding(8.dp),
                togglePaintingSize = togglePaintingSize,
                navigateBack = popBackStack,
                zoomIn = zoomInPainting,
                zoomOut = zoomOutPainting,
                screenHeight,
                screenWidth

            )

            val boxInABoxSize = paintingSize * 0.10f
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .size(boxInABoxSize),
                contentAlignment = Alignment.BottomEnd

            ) {
                Image(
                    painter = painterResource(R.drawable.baseline_touch_app_24),
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp).size(36.dp),
                    alpha = if (paintingState.zoom == ImageZoom.NotZoomed && paintingState.size == ImageSize.Small) 1f else 0f,
                )

                Image(
                    painter = painterResource(R.drawable.baseline_pinch_24),
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp).size(36.dp),
                    alpha = if (paintingState.size == ImageSize.Large && paintingState.zoom == ImageZoom.NotZoomed) 1f else 0f,
                )
            }
        }

        AnimatedVisibility(
            visible = paintingState.zoom == ImageZoom.NotZoomed && paintingState.size == ImageSize.Small,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
        ) {
            PaintingDetailsAndCartContent(
                painting = painting,
                cartScale = cartScale,
                onCartIconClick = onCartIconClick,
                currentPaintingCartItemCount = currentPaintingCartItemCount
            )
        }
    }
}

@Composable
fun CartIcon(
    cartIconScale: Float,
    onCartClick: () -> Unit,
    currentPaintingCartItemCount: Int
) {
    Box {
        Image(
            modifier = Modifier
                .scale(cartIconScale)
                .padding(8.dp)
                .clickable(onClick = onCartClick),
            painter = painterResource(R.drawable.baseline_add_shopping_cart_24),
            contentDescription = null,
        )
        if (currentPaintingCartItemCount != 0) {
            Text(
                text = currentPaintingCartItemCount.toString(),
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

@Composable
fun ZoomablePaintingImg(
    imageUrl: String,
    modifier: Modifier,
    togglePaintingSize: () -> Unit,
    navigateBack: () -> Unit,
    zoomIn: () -> Unit,
    zoomOut: () -> Unit,
    screenHeight: Float,
    screenWidth: Float,
    ) {
    var imageRotationAngle by remember { mutableStateOf(0f) }
    var imageZoomLevel by remember { mutableStateOf(1f) }
    var imageOffsetX by remember { mutableStateOf(0f) }
    var imageOffsetY by remember { mutableStateOf(0f) }

    fun resetImgAttributes() {
        imageZoomLevel = 1f
        imageOffsetX = 0f
        imageOffsetY = 0f
        imageRotationAngle = 0f
        zoomOut()
    }

    BackHandler(enabled = true) {
        if (isImageZoomed(imageZoomLevel, imageOffsetX, imageOffsetY, imageRotationAngle)) {
            resetImgAttributes()
        } else {
            navigateBack()
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
                            zoomIn()
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
                        togglePaintingSize()
                    }
                },
            )
    )
}

@Composable
fun PaintingDetailsAndCartContent(
    painting: Painting,
    cartScale: Float,
    onCartIconClick: () -> Unit,
    currentPaintingCartItemCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = painting.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )

            Text(
                //TODO add a size to the painting model and replace static var
                text = "65 x 56 cm",
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
            )

            Spacer(Modifier.weight(1f))

            CartIcon(
                cartIconScale = cartScale,
                onCartClick = onCartIconClick,
                currentPaintingCartItemCount = currentPaintingCartItemCount
            )
        }

        Text(
            text = painting.description,
            modifier = Modifier.padding(8.dp)
        )
    }
}


fun isImageZoomed(zoom: Float, offsetX: Float, offsetY: Float, angle: Float): Boolean {
    return (zoom != 1f || offsetX != 0f || offsetY != 0f || angle != 0f)
}

data class PaintingState(val size: ImageSize, val zoom: ImageZoom)
enum class ImageSize { Small, Large }
enum class ImageZoom { NotZoomed, Zoomed }
