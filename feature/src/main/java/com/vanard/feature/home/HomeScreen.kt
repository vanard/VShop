package com.vanard.feature.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.vanard.ui.components.responsiveProductGridItems
import androidx.compose.ui.graphics.Color
import com.vanard.ui.components.HeroPromoBanner
import com.vanard.ui.components.Spacer
import com.vanard.ui.theme.VShopBackground
import com.vanard.ui.theme.VShopDark
import com.vanard.ui.theme.VShopPrimary
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
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 18.dp,
                        end = 16.dp,
                        bottom = 24.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(if (showSearchResults) 20.dp else 22.dp)
                ) {
                    if (!showSearchResults) {
                        item {
                            HomeHeader(onProfileClick = { navController.navigate(Screen.Profile.route) })
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
                        SearchScreen(
                            viewModel = viewModel,
                            products = products,
                            navController = navController,
                            context = context
                        )
                    } else {
                        item {
                            HeroPromoBanner(
                                title = "Adidas Campus",
                                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.",
                                onActionClick = { /* Handle action */ }
                            )
                        }
                        item {
                            MostPopularSection(
                                title = "Most Popular",
                                products = products.products,
                                onSelectedProduct = ::openProduct,
                                onFavClick = { product ->
                                    viewModel.updateProductItem(product)
                                    context.toastMsg("${product.title.firstWords(2)} updated")
                                }
                            )
                        }
                        item {
                            BrandFeatureSection()
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
        HomeHeader(onProfileClick = {})
        SearchAndFilterBar(
            query = "",
            onQueryChanged = viewModel::onSearchTextChange,
            onFilterClick = {},
            onSearchClick = viewModel::submitSearch,
            onExitSearchClick = viewModel::exitSearch
        )
        LoadingSingleTop()
    }
}

@Composable
private fun HomeHeader(onProfileClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Current User",
            color = VShopTextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
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
private fun MostPopularSection(
    title: String,
    products: List<Product>,
    onSelectedProduct: (Product) -> Unit,
    onFavClick: (Product) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(title = title)
        products.chunked(2).forEach { rowProducts ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                rowProducts.forEach { product ->
                    ShopItemContent(
                        product = product,
                        onSelectedProduct = { onSelectedProduct(product) },
                        onFavClick = { onFavClick(product) },
                        modifier = Modifier.weight(1f),
                        fillWidth = true
                    )
                }
                if (rowProducts.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun BrandFeatureSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(VShopSurface)
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "POPULAR BRAND",
                color = VShopTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Lorem Ipsum Dolor",
                color = VShopTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                color = VShopTextSecondary,
                fontSize = 14.sp
            )
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = VShopPrimary),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "View Collections",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    VShopTheme {
        HomeScreen(rememberNavController())
    }
}
