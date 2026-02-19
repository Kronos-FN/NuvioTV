package com.nuvio.tv.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

/**
 * Cross-platform network image loader using Coil3
 */
@Composable
fun NetworkImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        alignment = alignment,
        alpha = alpha,
        colorFilter = colorFilter
    )
}
