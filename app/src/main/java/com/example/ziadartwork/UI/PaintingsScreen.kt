package com.example.ziadartwork.UI

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ziadartwork.R
import com.example.ziadartwork.Response
import com.example.ziadartwork.model.Painting


@Composable
fun PaintingsScreen(
    viewModel: MainActivityViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,

    ) {
    val paintings = viewModel.paintingsState.collectAsState()
    var sds: List<Painting>? = (paintings.value as Response.Success).data
    when (paintings.value) {

        is Response.Success -> TODO()
//            PaintingImage(
//            imageUrl = paintings.getValue(),
//            contentDescription = null
//        )
        is Response.Failure -> TODO()
        Response.Loading -> TODO()
        is Response.Success -> TODO()
    }

//    Scaffold(
//        topBar = {
//            TopBar()
//        },
//        content = {
////            PaintingImage(paintings.value)
//        }
//    )


}

@Composable
fun PaintingImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 8.dp
) {
    Surface(
        color = Color.LightGray,
        elevation = elevation,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            placeholder = painterResource(R.drawable.first),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}
