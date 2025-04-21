package com.vanard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

private val defaultModifier = Modifier
    .clip(RoundedCornerShape(16.dp))
    .fillMaxWidth()

@Composable
fun ProductImage(imageUrl: String, modifier: Modifier = defaultModifier) {
    val context = LocalContext.current
    val imageRequest = remember(imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
//            .size(coil.size.Size.ORIGINAL)
            .build()
    }

    SubcomposeAsyncImage(
        model = imageRequest,
//        model = imageUrl,
        contentDescription = "Product image",
        contentScale = ContentScale.Crop,
        modifier = modifier,
        loading = { LoadingState() }
    )
}

@Preview
@Composable
private fun ProductImagePreview() {
    ProductImage(
        imageUrl = "image url",
        modifier = defaultModifier.then(
            Modifier.background(
                brush = Brush.verticalGradient(listOf(Color.White, Color.Black))
            )
        )
    )
}