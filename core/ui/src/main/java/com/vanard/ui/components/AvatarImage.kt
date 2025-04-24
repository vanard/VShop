package com.vanard.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
fun AvatarImage(painter: Painter, modifier: Modifier = Modifier) {
    Image(
        painter = painter,
        contentScale = ContentScale.Crop,
        contentDescription = "Avatar Image",
        modifier = modifier
            .clip(CircleShape)
    )
}