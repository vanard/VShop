package com.vanard.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanard.common.util.toastMsg
import com.vanard.core.common.UIState
import com.vanard.domain.model.getAllCategories
import com.vanard.domain.model.getCategories
import com.vanard.resources.R
import com.vanard.ui.components.ChipGroup
import com.vanard.ui.components.CustomSearchBar
import com.vanard.ui.components.ShopItemContent
import com.vanard.ui.theme.VShopTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
//    navigateToDetail: (Long) -> Unit,
) {
    viewModel.uiState.collectAsState(initial = UIState.Loading).value.let { uiState ->
        when (uiState) {
            is UIState.Loading -> {
                viewModel.getProducts()
            }

            is UIState.Success -> {
                val context = LocalContext.current
                val scrollState = rememberLazyListState()
                val scrollGridState = rememberLazyStaggeredGridState()

                val searchText by viewModel.searchText.collectAsState()
                val products by viewModel.products.collectAsState()
                val isSearching by viewModel.isSearching.collectAsState()

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {
                    Row(modifier = modifier.padding(top = 20.dp)) {
                        Column(
                            modifier = modifier
                                .weight(1f, fill = false)
                                .padding(end = 24.dp)
                        ) {
                            Text(
                                text = "Hello, Welcome \uD83D\uDC4B",
                                fontSize = 14.sp,
                                modifier = modifier.weight(1f, fill = false)
                            )
                            Spacer(Modifier.size(8.dp))
                            Text(
                                text = "User Display Name ALJFIOSDJFOIDSJFLQj /n sfkejj kladf asdfk",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = modifier.weight(1f, fill = false)
                            )
                        }
                        Image(
                            painter = painterResource(R.drawable.product1),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = modifier
                                .clip(CircleShape)
                                .size(60.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }

                    Spacer(Modifier.size(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                    ) {
//                        Row(
//                            horizontalArrangement = Arrangement.Center,
//                            modifier = modifier
//                                .padding(end = 16.dp)
//                                .weight(1f)
//                                .border(
//                                    1.dp,
//                                    color = colorResource(R.color.paint_02),
//                                    shape = RoundedCornerShape(8.dp)
//                                )
//                                .padding(16.dp)
//                        ) {
//                            Icon(
//                                painter = painterResource(R.drawable.search_normal),
//                                tint = Color.Black,
//                                contentDescription = null,
//                                modifier = modifier
//                            )
//                            BasicTextField(
//                                TextFieldState(""),
//                                textStyle = TextStyle(fontSize = 16.sp),
//                                modifier = modifier.weight(1f)
//                            )
//                            AnimatedVisibility(visible = true) {
//                                Icon(
//                                    painter = painterResource(R.drawable.setting_4),
//                                    tint = Color.Black,
//                                    contentDescription = null
//                                )
//                            }
//                        }
                        CustomSearchBar(
                            query = searchText,
                            onQueryChanged = viewModel::onSearchTextChange,
                            modifier = modifier
                                .weight(1f)
                        )
                        IconButton(
                            onClick = {
                                context.toastMsg("Sort Click")
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
                        })

                    if (isSearching){
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    LazyVerticalStaggeredGrid(
                        state = scrollGridState,
                        columns = StaggeredGridCells.Fixed(2),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalItemSpacing = 24.dp,
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        content = {
                            items(
                                items = products.products,
                                key = { it.id }) { product ->
                                ShopItemContent(product, {
                                    context.toastMsg("Product ${product.title}")
                                }, {
                                    context.toastMsg("${product.title} Fav")
                                })
                            }
                        }
                    )
                }
            }

            is UIState.Error -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    VShopTheme {
//        Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        HomeScreen()
//        }
    }
}