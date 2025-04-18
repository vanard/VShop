package com.vanard.feature.whishlist

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanard.common.UIState
import com.vanard.feature.ComingSoonScreen

@Composable
fun WhishlistScreen(
    modifier: Modifier = Modifier,
    viewModel: WhishlistViewModel = hiltViewModel(),
) {
    viewModel.uiState.collectAsState().value.let { uiState ->
        when (uiState) {
            is UIState.Loading -> {
                viewModel.getAllProducts()
            }

            is UIState.Error -> {
                ComingSoonScreen()
            }

            is UIState.Success -> {
                val products by viewModel.products.collectAsState()
                val scrollState = rememberLazyListState()

                LazyColumn(
                    state = scrollState,
                    modifier = modifier.fillMaxHeight(),
                    content = {
                        items(
//                    items = carts.carts,
                            items = products.products,
                            key = { it.id }) { product ->
                            Text(product.title)
                        }

                    })
            }
        }

    }
}