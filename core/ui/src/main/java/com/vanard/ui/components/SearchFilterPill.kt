package com.vanard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanard.ui.theme.VShopStroke
import com.vanard.ui.theme.VShopSurface
import com.vanard.ui.theme.VShopTextSecondary

@Composable
fun SearchFilterPill(
    text: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, VShopStroke, RoundedCornerShape(4.dp))
            .background(VShopSurface)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        leadingIcon?.invoke()
        Text(text = text, color = VShopTextSecondary, fontSize = 14.sp)
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            tint = VShopTextSecondary,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
    }
}