package com.vanard.feature.wishlist

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanard.common.UIState
import com.vanard.feature.ErrorScreen
import com.vanard.resources.R
import com.vanard.ui.components.FavoriteItemContent
import com.vanard.ui.components.LoadingSingleTop
import com.vanard.ui.components.RemoveMenuActions

@Composable
fun WishlistScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WishlistViewModel = hiltViewModel(),
) {
    viewModel.uiState.collectAsState().value.let { uiState ->
        when (uiState) {
            is UIState.Loading -> {
                viewModel.getAllFavoriteProducts()
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    HeaderWishlistScreen(navigateBack)
                    LoadingSingleTop()
                }
            }

            is UIState.Error -> {
                ErrorScreen()
            }

            is UIState.Success -> {
                val products by viewModel.products.collectAsState()
                val scrollState = rememberLazyListState()
                var expandedIndex by remember { mutableStateOf<Int?>(null) }
//                var expanded by remember { mutableStateOf(false) }

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {

                    HeaderWishlistScreen(navigateBack)

                    LazyColumn(
                        state = scrollState,
                        modifier = modifier.fillMaxHeight(),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        content = {
                            itemsIndexed(
                                items = products.products,
                                key = { index, _ -> index }) { index, product ->
                                val menuActions = RemoveMenuActions(
                                    onMenuClick = {
//                                        expanded = true
                                        expandedIndex = index
                                        Log.d(TAG, "onMenuClick: ${expandedIndex == index}")
                                    },
                                    showMenu = expandedIndex == index,
                                    onDismissMenu = {
                                        expandedIndex = null
                                        Log.d(TAG, "onDismissMenu: ${expandedIndex == index}")
                                    },
                                    onRemoveItem = {
                                        viewModel.updateProductItem(product)
                                        Log.d(TAG, "onRemoveItem: ${expandedIndex == index}")
                                    }
                                )
                                FavoriteItemContent(
                                    product,
                                    onSelectedProduct = {},
                                    actions = menuActions
                                )
                            }

                        })
                }
            }
        }

    }
}

@Composable
fun HeaderWishlistScreen(navigateBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(top = 16.dp)
    ) {
        IconButton(onClick = {
            navigateBack()
        }) {
            Icon(
                painter = painterResource(R.drawable.arrow_left_1),
                contentDescription = null,
                modifier = modifier
                    .border(
                        1.dp,
                        color = colorResource(R.color.paint_03),
                        shape = CircleShape
                    )
                    .padding(8.dp)
            )
        }

        Text(
            "Wishlist",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = modifier
                .weight(1f)
                .offset(x = (-24).dp)
        )

    }
}

private const val TAG = "WishlistScreen"