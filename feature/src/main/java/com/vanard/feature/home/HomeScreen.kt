package com.vanard.feature.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.vanard.common.Screen
import com.vanard.common.UIState
import com.vanard.common.util.firstWords
import com.vanard.common.util.toastMsg
import com.vanard.domain.model.Categories
import com.vanard.domain.model.Product
import com.vanard.domain.model.User
import com.vanard.domain.model.getAllCategories
import com.vanard.domain.model.getCategories
import com.vanard.feature.ErrorScreen
import com.vanard.feature.base.BaseScreen
import com.vanard.resources.R
import com.vanard.ui.components.CategoryIconItem
import com.vanard.ui.components.FeaturedOfferCard
import com.vanard.ui.components.FilterChipButton
import com.vanard.ui.components.LoadingSingleTop
import com.vanard.ui.components.SearchAndFilterBar
import com.vanard.ui.components.ShopItemContent
import com.vanard.ui.theme.VShopBackground
import com.vanard.ui.theme.VShopStroke
import com.vanard.ui.theme.VShopSurface
import com.vanard.ui.theme.VShopTextPrimary
import com.vanard.ui.theme.VShopTextSecondary
import com.vanard.ui.theme.VShopTextTertiary
import com.vanard.ui.theme.VShopTheme
import kotlinx.coroutines.launch

object HomeScreenTestTag {
    const val SEARCH = "search"
    const val LAZY_LIST = "lazy_list"
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    BaseScreen(
        navController = navController,
        requireAuth = false,
        showLoading = false
    ) { user ->
        HomeContent(
            navController = navController,
            user = user,
            viewModel = viewModel,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeContent(
    navController: NavController,
    user: User?,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProducts()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VShopBackground)
    ) {
        when (uiState) {
            is UIState.Loading -> HomeLoadingContent(viewModel = viewModel)
            is UIState.Success -> {
                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()
                val scrollState = rememberLazyListState()
                val products by viewModel.products.collectAsState()
                val searchText by viewModel.searchText.collectAsState()
                val submittedSearchText by viewModel.submittedSearchText.collectAsState()

                fun openProduct(product: Product) {
                    navController.navigate(Screen.Detail.detailRoute(product.id))
                }

                val showSearchResults = submittedSearchText.isNotBlank()

                LazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(HomeScreenTestTag.LAZY_LIST),
                    contentPadding = PaddingValues(start = 16.dp, top = 18.dp, end = 16.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(if (showSearchResults) 20.dp else 22.dp)
                ) {
                    if (!showSearchResults) {
                        item {
                            HomeLocationHeader(
                                user = user,
                                onProfileClick = { navController.navigate(Screen.Profile.route) }
                            )
                        }
                    }

                    item {
                        SearchAndFilterBar(
                            query = searchText,
                            onQueryChanged = viewModel::onSearchTextChange,
                            onFilterClick = {
                                viewModel.sortProducts()
                                coroutineScope.launch {
                                    withFrameNanos { }
                                    scrollState.animateScrollToItem(0)
                                }
                            },
                            modifier = Modifier.testTag(HomeScreenTestTag.SEARCH),
                            placeholder = if (showSearchResults) submittedSearchText else "Search Products",
                            isSearchMode = showSearchResults,
                            onSearchClick = {
                                viewModel.submitSearch()
                                coroutineScope.launch {
                                    withFrameNanos { }
                                    scrollState.animateScrollToItem(0)
                                }
                            },
                            onExitSearchClick = viewModel::exitSearch
                        )
                    }

                    if (showSearchResults) {
                        item { SearchResultFilters() }

                        searchResultGridItems(
                            items = products.products
                        ) { index, product ->
                            ShopItemContent(
                                product = product,
                                onSelectedProduct = { openProduct(product) },
                                onFavClick = {
                                    viewModel.updateProductItem(product)
                                    context.toastMsg("${product.title.firstWords(2)} updated")
                                },
                                badgeText = when (index) {
                                    0 -> "Featured"
                                    3, 4 -> "Free Delivery"
                                    else -> null
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        item { ProductCategories() }
                        item { ExclusiveOffers() }
                        item {
                            ProductRowSection(
                                title = "Trending Now",
                                products = products.products.take(6),
                                onSelectedProduct = ::openProduct,
                                onFavClick = { product ->
                                    val message = if (product.isFavorite) {
                                        "${product.title.firstWords(2)} has been removed from your favorites."
                                    } else {
                                        "${product.title.firstWords(2)} has been added to your favorites."
                                    }
                                    viewModel.updateProductItem(product)
                                    context.toastMsg(message)
                                }
                            )
                        }

                        item {
                            JustForYouFilters(
                                selectedCategory = viewModel.selectedCategory.value,
                                onSelectedCategory = {
                                    viewModel.selectCategory(getCategories(it))
                                }
                            )
                        }

                        responsiveProductGridItems(
                            items = products.products
                        ) { index, product ->
                            ShopItemContent(
                                product = product,
                                onSelectedProduct = { openProduct(product) },
                                onFavClick = {
                                    viewModel.updateProductItem(product)
                                    context.toastMsg("${product.title.firstWords(2)} updated")
                                },
                                badgeText = if (index % 3 == 0) "Featured" else null,
                                modifier = Modifier.fillMaxWidth(),
                                fillWidth = true
                            )
                        }
                    }
                }
            }

            is UIState.Error -> ErrorScreen()
            UIState.Idle -> Unit
        }
    }
}

@Composable
private fun HomeLoadingContent(viewModel: HomeViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VShopBackground)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        HomeLocationHeader(user = null, onProfileClick = {})
        SearchAndFilterBar(
            query = "",
            onQueryChanged = viewModel::onSearchTextChange,
            onFilterClick = {},
            onSearchClick = viewModel::submitSearch,
            onExitSearchClick = viewModel::exitSearch
        )
        ProductCategories()
        LoadingSingleTop()
    }
}

@Composable
private fun HomeLocationHeader(
    user: User?,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Current Location", color = VShopTextTertiary, fontSize = 10.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = user?.let { "${it.firstName} City" } ?: "Waterloo, Canada",
                    color = VShopTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    painter = painterResource(R.drawable.arrow_right_1),
                    contentDescription = null,
                    tint = VShopTextPrimary,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(13.dp)
                )
            }
        }
        IconButton(onClick = onProfileClick, modifier = Modifier.size(40.dp)) {
            Icon(
                painter = painterResource(R.drawable.profile_circle),
                contentDescription = "Profile",
                tint = VShopTextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun ProductCategories() {
    val categories = listOf(
        "Watch" to R.drawable.shopping_bag,
        "Mobile" to R.drawable.ic_phone,
        "Books" to R.drawable.category,
        "Cars" to R.drawable.shopping_cart,
        "Laptop" to R.drawable.profile_bulk,
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(title = "Product Categories")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            categories.forEach { (title, icon) ->
                CategoryIconItem(title = title, iconRes = icon)
            }
        }
    }
}

@Composable
private fun ExclusiveOffers() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Exclusive Offers", color = VShopTextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
            FeaturedOfferCard(title = "20% OFF", subtitle = "Up to", action = "Shop Now", modifier = Modifier.width(220.dp))
            FeaturedOfferCard(title = "40 USD", subtitle = "Order over $", action = "Cashback", modifier = Modifier.width(220.dp))
        }
    }
}

@Composable
private fun ProductRowSection(
    title: String,
    products: List<Product>,
    onSelectedProduct: (Product) -> Unit,
    onFavClick: (Product) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(title = title)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            itemsIndexed(products, key = { _, item -> item.id }) { index, product ->
                ShopItemContent(
                    product = product,
                    onSelectedProduct = { onSelectedProduct(product) },
                    onFavClick = { onFavClick(product) },
                    badgeText = if (index == 0) "4.9(2k)" else null
                )
            }
        }
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

@Composable
private fun SearchFilterPill(
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

@Composable
private fun JustForYouFilters(
    selectedCategory: Categories?,
    onSelectedCategory: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Just For You", color = VShopTextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
            FilterChipButton(text = "Filter") {
                Icon(
                    painter = painterResource(R.drawable.setting_4),
                    tint = VShopTextSecondary,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            }
            getAllCategories().forEach { category ->
                FilterChipButton(
                    text = if (category == selectedCategory) "${category.value} ✓" else category.value,
                    onClick = { onSelectedCategory(category.value) },
                    modifier = Modifier.clip(RoundedCornerShape(6.dp))
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = VShopTextPrimary,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )
        TextButton(onClick = {}) {
            Text(text = "See All", color = VShopTextSecondary, fontSize = 12.sp)
        }
    }
}

private fun LazyListScope.responsiveProductGridItems(
    items: List<Product>,
    itemContent: @Composable (Int, Product) -> Unit,
) {
    item(key = "responsive-product-grid") {
        val screenWidth = with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
        val columns = if (screenWidth >= 600.dp) 4 else 2
        val rowSpacing = if (columns == 4) 24.dp else 22.dp
        val columnSpacing = if (columns == 4) 16.dp else 14.dp

        Column(verticalArrangement = Arrangement.spacedBy(rowSpacing)) {
            items.chunked(columns).forEachIndexed { rowIndex, rowItems ->
                Row(horizontalArrangement = Arrangement.spacedBy(columnSpacing), modifier = Modifier.fillMaxWidth()) {
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    VShopTheme {
        HomeScreen(rememberNavController())
    }
}
