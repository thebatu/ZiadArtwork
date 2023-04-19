package com.example.ziadartwork.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ziadartwork.R

private enum class ImageState {
    Small, Large
}

@Composable
fun MyUI() {
    var imageState by remember {
        mutableStateOf(ImageState.Small)
    }

    val transition = updateTransition(targetState = imageState, label = "BoxState Transition")

    val borderColor by
        transition.animateColor(label = "BoxState Color Transition") {
            when (it) {
                ImageState.Small -> Color.Green
                ImageState.Large -> Color.Magenta
            }
        }

    val size by transition.animateDp(label = "BoxState Size Transition") {
        when (it) {
            ImageState.Small -> 200.dp
            ImageState.Large -> 400.dp
        }
    }

    val paintingOffset by transition.animateOffset(transitionSpec = {
        if (this.initialState == ImageState.Small) {
            tween(400) // launch duration
        } else {
            tween(600) // land duration
        }
    }, label = "painting offset") { animated ->
        if (animated == ImageState.Large) {
            Offset(0f, 40f)
        } else {
            Offset(0f, 0f)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .size(size = size)
                .clip(shape = CircleShape)
                .border(color = borderColor, shape = CircleShape, width = 3.dp)
                .offset(paintingOffset.x.dp, paintingOffset.y.dp),
            painter = painterResource(id = R.drawable.first),
            contentDescription = "Dog",
        )

        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = {
                imageState =
                    if (imageState == ImageState.Small) {
                        ImageState.Large
                    } else {
                        ImageState.Small
                    }
            },
        ) {
            Text(text = "Toggle State")
        }
    }
}
