package com.vanard.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.vanard.domain.model.Product

fun LazyListScope.responsiveProductGridItems(
    items: List<Product>,
    itemContent: @Composable (Int, Product) -> Unit,
) {
    item(key = "responsive-product-grid") {
        val screenWidth =
            with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
        val columns = if (screenWidth >= 600.dp) 4 else 2
        val rowSpacing = if (columns == 4) 24.dp else 22.dp
        val columnSpacing = if (columns == 4) 16.dp else 14.dp

        Column(verticalArrangement = Arrangement.spacedBy(rowSpacing)) {
            items.chunked(columns).forEachIndexed { rowIndex, rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(columnSpacing),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEachIndexed { columnIndex, product ->
                        Box(modifier = Modifier.weight(1f)) {
                            itemContent(rowIndex * columns + columnIndex, product)
                        }
                    }
                    repeat(columns - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}