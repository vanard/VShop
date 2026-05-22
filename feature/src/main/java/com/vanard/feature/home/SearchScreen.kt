package com.vanard.feature.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vanard.common.Screen
import com.vanard.common.util.firstWords
import com.vanard.common.util.toastMsg
import com.vanard.domain.model.Product
import com.vanard.domain.model.ProductList
import com.vanard.resources.R
import com.vanard.ui.components.SearchFilterPill
import com.vanard.ui.components.ShopItemContent
import com.vanard.ui.components.responsiveProductGridItems
import com.vanard.ui.theme.VShopTextPrimary
import kotlinx.coroutines.CoroutineScope

@Composable
fun LazyListScope.SearchScreen(
    viewModel: HomeViewModel,
    products: ProductList,
    navController: NavController,
) {
    // Context for toast messages
    val context = LocalContext.current

    // Search results header
    SearchResultFilters()

    // Render the product grid for search results
    responsiveProductGridItems(
        items = products.products
    ) { index, product ->
        ShopItemContent(
            product = product,
            onSelectedProduct = {
                navController.navigate(Screen.Detail.detailRoute(product.id))
            },
            onFavClick = {
                viewModel.updateProductItem(product)
                context.toastMsg("${product.title.firstWords(2)} updated")
            },
            badgeText = when (index) {
                0 -> "Featured"
                3, 4 -> "Free Delivery"
                else -> null
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SearchResultFilters() {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SearchFilterPill(
            text = "Sort By",
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.menu),
                    tint = VShopTextPrimary,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            }
        )
        SearchFilterPill(text = "Brand")
        SearchFilterPill(text = "Ram Memory")
        SearchFilterPill(text = "Storage")
    }
}

private fun LazyListScope.searchResultGridItems(
    items: List<Product>,
    itemContent: @Composable (Int, Product) -> Unit,
) {
    items.chunked(2).forEachIndexed { rowIndex, rowItems ->
        item(key = "search-grid-$rowIndex") {
            Row(horizontalArrangement = Arrangement.spacedBy(22.dp), modifier = Modifier.fillMaxWidth()) {
                rowItems.forEachIndexed { columnIndex, product ->
                    Box(modifier = Modifier.weight(1f)) {
                        itemContent(rowIndex * 2 + columnIndex, product)
                    }
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
