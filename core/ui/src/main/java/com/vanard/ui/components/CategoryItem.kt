package com.vanard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanard.resources.R
import com.vanard.ui.theme.VShopDark
import com.vanard.ui.theme.VShopSoftSurface
import com.vanard.ui.theme.VShopSurface
import com.vanard.ui.theme.VShopTextSecondary
import com.vanard.ui.theme.VShopTheme

@Composable
fun CategoryItemContent(
    category: String,
    isSelected: Boolean = false,
    onSelectionChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isSelected) VShopDark else VShopSoftSurface
    val contentColor = if (isSelected) VShopSurface else VShopTextSecondary

    Surface(
        color = containerColor,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.clickable { onSelectionChanged(category) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = category,
                color = contentColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun CategoryIconItem(
    title: String,
    iconRes: Int = R.drawable.category,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = title,
            tint = VShopTextSecondary,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(VShopSoftSurface)
                .padding(12.dp)
        )
        Text(
            text = title,
            color = VShopTextSecondary,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    VShopTheme {
        Row(modifier = Modifier.padding(16.dp)) {
            CategoryIconItem("Mobile")
        }
    }
}
