package com.vanard.feature.home

import android.content.Context
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vanard.common.Screen
import com.vanard.common.util.firstWords
import com.vanard.common.util.toastMsg
import com.vanard.domain.model.ProductList
import com.vanard.resources.R
import com.vanard.ui.components.SearchFilterPill
import com.vanard.ui.components.ShopItemContent
import com.vanard.ui.components.responsiveProductGridItems
import com.vanard.ui.theme.VShopTextPrimary

fun LazyListScope.SearchScreen(
    viewModel: HomeViewModel,
    products: ProductList,
    navController: NavController,
    context: Context,
) {
    // Search results header
    item {
        SearchResultFilters()
    }

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
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
