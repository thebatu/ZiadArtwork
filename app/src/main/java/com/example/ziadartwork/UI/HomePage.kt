package com.example.ziadartwork.UI

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ziadartwork.model.Painting

@Composable
fun HomePage(modifier: Modifier, list: List<Painting>) {
    val scaffoldState = rememberScaffoldState()

//    LazyColumn(
//        modifier = modifier,
//        horizontalAlignment = Alignment.CenterHorizontally,
//
//        ) {
//        itemsIndexed(list) { index, painting ->
//            Tableau(drawable = painting.url, text = painting.name )
//        }
//
//    }


}


@Composable
fun Tableau(drawable: String, text: String) {

//    Image(
//        painter = painterResource(drawable),
//        contentDescription = null,
//        contentScale = ContentScale.Crop,
//        modifier = Modifier
//    )
//    Text(
//        text = stringResource(text),
//        style = typography.h3,
//        modifier = Modifier.paddingFromBaseline(
//            top = 24.dp, bottom = 8.dp
//        )
//    )

}