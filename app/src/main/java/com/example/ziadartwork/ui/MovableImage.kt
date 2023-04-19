package com.example.ziadartwork.ui

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest

@Composable
fun TransitionAnimation() {
    var isAnimated by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = isAnimated, label = "transition")

    val paintingOffset by transition.animateOffset(transitionSpec = {
        if (this.targetState) {
            tween(1000) // launch duration
        } else {
            tween(1500) // land duration
        }
    }, label = "painting offset") { animated ->
        if (animated) Offset(200f, 0f) else Offset(200f, 500f)
    }

    val paintingSize by transition.animateDp(transitionSpec = {
        tween(1000)
    }, "") { animated ->
        if (animated) 75.dp else 450.dp
    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://firebasestorage.googleapis.com/v0/b/ziad-artwork.appspot.com/o/chenie_wrapped.webp?alt=media&token=fec82161-c191-4298-ab92-9716230d8580")
            .memoryCachePolicy(CachePolicy.DISABLED)
            .build()
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Image(
            painter = painter,
            contentDescription = "Rocket",
            modifier = Modifier
                .offset(paintingOffset.x.dp, paintingOffset.y.dp)
                .size(paintingSize),
            contentScale = ContentScale.Crop,
        )

        Button(
            onClick = { isAnimated = !isAnimated },
            modifier = Modifier.padding(top = 10.dp),
        ) {
            Text(text = if (isAnimated) "Land rocket" else "Launch rocket")
        }
    }
}
