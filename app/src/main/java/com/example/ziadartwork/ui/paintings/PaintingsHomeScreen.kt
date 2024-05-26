package com.example.ziadartwork.ui.paintings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.ziadartwork.navigation.Destination
import com.example.ziadartwork.R
import com.example.ziadartwork.data.model.Painting
import com.example.ziadartwork.theme.LocalDimensions
import com.example.ziadartwork.theme.dimensions
import com.example.ziadartwork.ui.paintings.MainActivityViewModel.PaintingsUiState.*

@Composable
fun PaintingsHomeScreen(
    onPaintingSelected: (String) -> Unit = {},
    viewModel: MainActivityViewModel = hiltViewModel(),
) {

    val homeScreenState by remember(viewModel) {
        viewModel.fetchPaintings
    }.collectAsStateWithLifecycle(Loading)

    when (val state = homeScreenState) {
        is Loading -> LoadingScreen()
        is Error -> ErrorScreen(retryAction = { viewModel.fetchPaintings })
        is Success -> {
            val paintingList = state.data
            PaintingsItemList(
                paintingsList = paintingList,
                onPaintingSelected = onPaintingSelected
            )
        }
    }
}

@Composable
fun PaintingsItemList(
    paintingsList: List<Painting>,
    onPaintingSelected: (String) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 200.dp),
        modifier = Modifier.fillMaxHeight(),
    ) {
        items(paintingsList, key = { item -> item.id }) { painting ->
            PaintingItem(
                imageUrl = painting.url,
                contentDescription = painting.description,
                name = painting.name,
                id = painting.id,
                onPaintingSelected = onPaintingSelected,

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
    onPaintingSelected: (String) -> Unit,
) {
    Card(
        modifier = modifier
            .padding(MaterialTheme.dimensions.extraSmall)
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = MaterialTheme.dimensions.small,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .setHeader("CacheControl", "maxage=31536000")
                .networkCachePolicy(CachePolicy.ENABLED)
                .build(),
            placeholder = painterResource(R.drawable.ic_broken_image),
            error = painterResource(R.drawable.ic_broken_image),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimensions.extraSmall)
                .clickable(
                    onClick = {
                        onPaintingSelected(Destination.DetailDestination.withArgs(id))
                    },
                )
                .border(BorderStroke(1.dp, Color.Black)),
        )
    }
}

/**
 * The home screen displaying error message with reattempt button.
 */
@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
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
        modifier = modifier.fillMaxSize(),
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading),
        )
    }
}
