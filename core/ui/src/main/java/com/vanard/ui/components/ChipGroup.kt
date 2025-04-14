package com.vanard.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vanard.domain.model.Categories
import com.vanard.domain.model.getAllCategories

@Preview(showBackground = true)
@Composable
fun ChipGroup(
    state: LazyListState = rememberLazyListState(),
    categories: List<Categories> = getAllCategories(),
    selectedCategories: Categories? = Categories.AllItems,
    onSelectedChanged: (String) -> Unit = {},
) {
    LazyRow(
        state = state,
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