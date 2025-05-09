package com.vanard.feature.home

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.vanard.domain.model.getAllCategories
import com.vanard.domain.model.getCategories
import com.vanard.feature.ErrorScreen
import com.vanard.resources.R
import com.vanard.ui.components.AvatarImage
import com.vanard.ui.components.ChipGroup
import com.vanard.ui.components.CustomSearchBar
import com.vanard.ui.components.LoadingSingleTop
import com.vanard.ui.components.ShopItemContent
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
//    viewModel.uiState.collectAsState(initial = UIState.Loading).value.let { uiState ->
    viewModel.uiState.collectAsState().value.let { uiState ->
        when (uiState) {
            is UIState.Loading -> {
                viewModel.getProducts()
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    HeaderHomeScreen()

                    Spacer(Modifier.size(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                    ) {
                        CustomSearchBar(
                            query = "",
                            onQueryChanged = viewModel::onSearchTextChange,
                            modifier = modifier
                                .weight(1f)
                        )
                        IconButton(
                            onClick = {},
                            modifier = modifier
                                .padding(start = 16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(color = colorResource(R.color.paint_01))
                                .size(56.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.setting_4),
                                tint = Color.White,
                                contentDescription = "Icon"
                            )
                        }
                    }

                    ChipGroup(
                        categories = getAllCategories(),
                        selectedCategories = viewModel.selectedCategory.value,
                        onSelectedChanged = {}
                    )

                    LoadingSingleTop()
                }
            }

            is UIState.Success -> {
                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()
                val scrollState = rememberLazyListState()
                val scrollGridState = rememberLazyStaggeredGridState()

                val searchText by viewModel.searchText.collectAsState()
                val products by viewModel.products.collectAsState()
                val isSearching by viewModel.isSearching.collectAsState()

//                LaunchedEffect(products) {
//                    println("Products updated: ${products.products.map { it.isFavorite }}")
//                    Log.d(
//                        "HomeScreen",
//                        "Products updated: ${products.products.map { it.isFavorite }}"
//                    )
//                }

                fun scrollToTop() {
                    coroutineScope.launch {
//                            delay(10)
                        withFrameNanos { }
                        scrollGridState.scrollToItem(0)
                    }
                }

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    HeaderHomeScreen()

                    Spacer(Modifier.size(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                    ) {
                        CustomSearchBar(
                            query = searchText,
                            onQueryChanged = viewModel::onSearchTextChange,
                            modifier = modifier
                                .weight(1f)
                                .testTag(HomeScreenTestTag.SEARCH)
                        )
                        IconButton(
                            onClick = {
                                viewModel.sortProducts()
                                scrollToTop()
                            },
                            modifier = modifier
                                .padding(start = 16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(color = colorResource(R.color.paint_01))
                                .size(56.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.setting_4),
                                tint = Color.White,
                                contentDescription = null
                            )
                        }
                    }

                    ChipGroup(
                        state = scrollState,
                        categories = getAllCategories(),
                        selectedCategories = viewModel.selectedCategory.value,
                        onSelectedChanged = {
                            viewModel.selectCategory(getCategories(it))
                            scrollToTop()
                        })

                    if (isSearching) {
                        LoadingSingleTop()
                    }

                    LazyVerticalStaggeredGrid(
                        state = scrollGridState,
                        columns = StaggeredGridCells.Fixed(2),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalItemSpacing = 24.dp,
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.testTag(HomeScreenTestTag.LAZY_LIST),
                        content = {
                            items(
                                items = products.products,
                                key = { it.id }) { product ->
                                ShopItemContent(
                                    product,
                                    onSelectedProduct = {
                                        navController.navigate(
                                            route = Screen.Detail.detailRoute(
                                                product.id
                                            )
                                        )
                                    },
                                    onFavClick = {
                                        val message = if (product.isFavorite)
                                            "${product.title.firstWords(2)} has been removed from your favorites."
                                        else
                                            "${product.title.firstWords(2)} has been added to your favorites."

                                        viewModel.updateProductItem(product)
                                        context.toastMsg(message)
                                    },
                                    modifier = modifier.animateItem(
                                        tween(300)
                                    ).testTag(product.title)
                                )
                            }
                        }
                    )
                }
            }

            is UIState.Error -> {
                ErrorScreen()
            }
        }
    }
}

@Composable
fun HeaderHomeScreen(modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(top = 20.dp)) {
        Column(
            modifier = modifier
                .weight(1f)
                .padding(end = 24.dp)
        ) {
            Text(
                text = "Hello, Welcome \uD83D\uDC4B",
                fontSize = 14.sp,
                modifier = modifier.weight(1f, fill = false)
            )

            Spacer(Modifier.size(8.dp))

            Text(
                text = "User Guest",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = modifier.weight(1f, fill = false)
            )
        }

        AvatarImage(
            painter = painterResource(R.drawable.product1),
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    VShopTheme {
//        Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        HomeScreen(rememberNavController())
//        }
    }
}