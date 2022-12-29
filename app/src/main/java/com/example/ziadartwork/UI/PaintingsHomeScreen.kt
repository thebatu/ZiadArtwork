package com.example.ziadartwork.UI

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.ziadartwork.R
import com.example.ziadartwork.Response
import com.example.ziadartwork.model.Painting
import kotlin.reflect.KFunction1


@Composable
fun PaintingsHomeScreen(
    modifier: Modifier = Modifier,
    retryAction: ()-> Unit,
    paintingsScreenState: Response<List<Painting>>

) {
    when (paintingsScreenState) {
        is Response.Success ->
            (paintingsScreenState).data?.let {
                PaintingsItemList(
                    paintingsList = it
                )
            }
        is Response.Failure -> ErrorScreen(retryAction = { retryAction })
        is Response.Loading -> LoadingScreen()
    }

//    Scaffold(
//        topBar = {
//            TopAppBar()
//        },

}

@Composable
fun PaintingsItemList(paintingsList: List<Painting>) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 200.dp),

        modifier = Modifier.fillMaxHeight()
    ) {
        items(paintingsList, key = { item -> item.id }) { painting ->
            PaintingItem(
                imageUrl = painting.url,
                contentDescription = painting.description,
                name = painting.name,
                id = painting.id
            )
        }
    }
}

@Composable
fun PaintingItem(
    imageUrl: String,
    name: String,
    id: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = 8.dp
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .setHeader("Cache-Control", "max-age=31536000")
                .networkCachePolicy(CachePolicy.ENABLED)
                .build(),
            placeholder = painterResource(R.drawable.ic_broken_image),
            error = painterResource(R.drawable.ic_broken_image),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .border(BorderStroke(1.dp, Color.Black)),
        )
    }
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.loading_failed))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading)
        )
    }
}

//@Composable
//fun TopAppBar() {
//    topBar = (
//    title = { Text(text = "Top App Bar") },
//    navigationIcon = {
//        IconButton(onClick = {}) {
//            Icon(Icons.Filled.ArrowBack, "backIcon")
//        }
//    },
//    backgroundColor = MaterialTheme.colors.primary,
//    contentColor = Color.White,
//    elevation = 10.dp
//    )


//}



