package com.vanard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanard.ui.theme.VShopDark
import com.vanard.ui.theme.VShopHeroEnd
import com.vanard.ui.theme.VShopHeroStart
import com.vanard.ui.theme.VShopPrimary
import com.vanard.ui.theme.VShopSurface
import com.vanard.ui.theme.VShopTextPrimary
import com.vanard.ui.theme.VShopTextSecondary

@Composable
fun HeroPromoBanner(
    title: String = "Adidas Campus",
    description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.",
    onActionClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(VShopHeroStart, VShopHeroEnd)))
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "OUR EXCLUSIVE",
                        color = VShopPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = title,
                        color = VShopTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = description,
                        color = VShopTextSecondary,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Button(
                    onClick = onActionClick,
                    colors = ButtonDefaults.buttonColors(containerColor = VShopDark),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "View Collections",
                        color = VShopSurface,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Product Image Placeholder (as in Figma)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                // In a real app, we'd use a specific image resource or Coil
                // Using a placeholder that looks like a shoe for now
                Text(text = "👟", fontSize = 64.sp)
            }
        }

        // Pagination Dots
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == 0) 16.dp else 6.dp)
                        .clip(CircleShape)
                        .background(if (index == 0) VShopPrimary else Color(0xFFFED7AA))
                )
                if (index < 2) Spacer(modifier = Modifier.size(6.dp))
            }
        }
    }
}

@Composable
fun Spacer(modifier: Modifier) {
    androidx.compose.foundation.layout.Spacer(modifier = modifier)
}
