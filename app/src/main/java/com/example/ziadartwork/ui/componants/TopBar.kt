package com.example.ziadartwork.ui.componants

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.ziadartwork.R

@Composable
fun TopBar(onCartIconClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            IconButton(onClick =  onCartIconClick ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Shopping Cart Iionm"
                )
            }
        }
    )
}
