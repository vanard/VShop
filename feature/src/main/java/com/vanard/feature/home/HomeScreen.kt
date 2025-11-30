package com.vanard.feature.home

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells.Fixed
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.vanard.domain.model.User
import com.vanard.domain.model.getAllCategories
import com.vanard.domain.model.getCategories
import com.vanard.feature.ErrorScreen
import com.vanard.feature.base.BaseScreen
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
    // Use BaseScreen to provide optional user data
    BaseScreen(
        navController = navController,
        requireAuth = false, // Don't require auth, but provide user data if available
        showLoading = false // Don't show loading for home screen
    ) { user ->
        HomeContent(
            navController = navController,
            user = user, // This will be null if not authenticated
            viewModel = viewModel,
            modifier = modifier
        )
    }
}

@Composable
private fun HomeContent(
    navController: NavController,
    user: User?,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val products by viewModel.products.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val selectedCategory = viewModel.selectedCategory.value
    
    LaunchedEffect(Unit) {
        viewModel.getProducts()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        // Header with user greeting (if authenticated)
        if (user != null && user.id.isNotEmpty()) {
            HomeHeaderLogin()
        } else {
            HomeHeaderGuest(
                user = user,
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onLoginClick = { navController.navigate(Screen.Login.route) }
            )
        }
        
        // Rest of your existing home screen content...
        // SearchBar, Categories, Products, etc.
        
        // Your existing HomeScreen content here
        when (uiState) {
            is UIState.Loading -> {
                viewModel.getProducts()
//                Column(
//                    modifier = modifier
//                        .fillMaxSize()
//                        .padding(horizontal = 20.dp)
//                ) {
//                    HomeHeaderLogin()

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
//                }
            }

            is UIState.Success -> {
                val context = LocalContext.current
                val coroutineScope = rememberCoroutineScope()
                val scrollState = rememberLazyListState()
                val scrollGridState = rememberLazyStaggeredGridState()

                val products by viewModel.products.collectAsState()
                val searchText by viewModel.searchText.collectAsState()
                val isSearching by viewModel.isSearching.collectAsState()

                fun scrollToTop() {
                    coroutineScope.launch {
//                            delay(10)
                        withFrameNanos { }
                        scrollGridState.scrollToItem(0)
                    }
                }

//                Column(
//                    modifier = modifier
//                        .fillMaxSize()
//                        .padding(horizontal = 20.dp)
//                ) {
//                    HomeHeaderLogin()

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
                        columns = Fixed(2),
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
                                    modifier = modifier
                                        .animateItem(
                                            tween(300)
                                        )
                                        .testTag(product.title)
                                )
                            }
                        }
                    )
//                }
            }

            is UIState.Error -> {
                ErrorScreen()
            }

            UIState.Idle -> {}
        }
    }
}

@Composable
private fun HomeHeaderGuest(
    user: User?,
    onProfileClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.paint_04)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (user != null) "Hello, ${user.firstName}!" else "Welcome, Guest!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.paint_01)
                )
                Text(
                    text = if (user != null) "Ready to shop?" else "Sign in for personalized experience",
                    fontSize = 14.sp,
                    color = colorResource(R.color.paint_02)
                )
            }
            
            if (user != null) {
                IconButton(onClick = onProfileClick) {
                    Icon(
                        painter = painterResource(R.drawable.profile),
                        contentDescription = "Profile",
                        tint = colorResource(R.color.paint_01)
                    )
                }
            } else {
                TextButton(onClick = onLoginClick) {
                    Text(
                        text = "Sign In",
                        color = colorResource(R.color.paint_01),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun HomeHeaderLogin(modifier: Modifier = Modifier) {
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