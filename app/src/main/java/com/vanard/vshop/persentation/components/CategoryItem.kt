package com.vanard.vshop.persentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanard.vshop.R
import com.vanard.vshop.persentation.ui.theme.VShopTheme

@Composable
fun CategoryItemContent(
    category: String, isSelected: Boolean = false,
    onSelectionChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val shapeBg = RoundedCornerShape(8.dp)
    Surface(shadowElevation = 4.dp, modifier = modifier.clip(shapeBg)) {
        Row(
            modifier = modifier
                .toggleable(value = isSelected, onValueChange = {
                    onSelectionChanged(category)
                })
                .border(
                    1.dp,
                    color = if (isSelected)
                        colorResource(R.color.paint_01)
                    else colorResource(R.color.paint_03),
                    shape = shapeBg
                )
                .background(
                    color = if (isSelected)
                        colorResource(R.color.paint_01)
                    else
                        Color.Transparent,
                    shape = shapeBg
                )
                .padding(12.dp)
        ) {
            Text(
                fontSize = 12.sp, text = category, color = if (isSelected)
                    colorResource(R.color.white)
                else colorResource(R.color.paint_05)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    VShopTheme {
        CategoryItemContent("dummyProduct")
    }
}