package com.vanard.vshop.persentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vanard.vshop.domain.model.Categories
import com.vanard.vshop.domain.model.getAllCategories

@Preview(showBackground = true)
@Composable
fun ChipGroup(
    categories: List<Categories> = getAllCategories(),
    selectedCategories: Categories? = Categories.AllItems,
    onSelectedChanged: (String) -> Unit = {},
) {
    LazyRow(
        contentPadding = PaddingValues(vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) {
            CategoryItemContent(
                category = it.value,
                isSelected = selectedCategories == it,
                onSelectionChanged = { category ->
                    onSelectedChanged(category)
                },
            )
        }
    }
}