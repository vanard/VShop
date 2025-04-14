package com.vanard.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.vanard.ui.theme.VShopTheme

@Composable
fun ComingSoonScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            "Coming Soon \uD83D\uDC4B",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ComingSoonScreenPreview(modifier: Modifier = Modifier) {
    VShopTheme {
        ComingSoonScreen()
    }
}