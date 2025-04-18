package com.vanard.feature.cart

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanard.common.UIState
import com.vanard.common.util.toastMsg
import com.vanard.feature.ComingSoonScreen
import com.vanard.resources.R
import com.vanard.ui.components.CartItemContent
import com.vanard.ui.components.NormalText
import com.vanard.ui.theme.VShopTheme

@Composable
fun CartScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel()
) {
    viewModel.uiState.collectAsState().value.let { uiState ->
        when (uiState) {
            is UIState.Loading -> {
                viewModel.getCarts()
                viewModel.getLocalCarts()

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    HeaderCartScreen(navigateBack)
                    Spacer(modifier = Modifier.weight(1f))
                    FooterCartScreen(0.0)
                }
            }

            is UIState.Success -> {
                var total = 0.0

                val configuration = LocalConfiguration.current
                val screenHeight = configuration.screenHeightDp.dp

                val context = LocalContext.current
//    val carts by viewModel.carts.collectAsState()
                val carts by viewModel.localCarts.collectAsState()
                val scrollState = rememberLazyListState()

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    HeaderCartScreen(navigateBack)

                    LazyColumn(
                        state = scrollState,
                        modifier = modifier.size((screenHeight / 2) - 36.dp),
                        content = {
                            items(
//                    items = carts.carts,
                                items = carts,
                                key = { it.id }) { cart ->
                                CartItemContent(
                                    cart,
                                    onSelectedProduct = {
                                        context.toastMsg("Product Cart")
                                    },
                                )
                            }

                        })

                    FooterCartScreen(total)
                }
            }

            is UIState.Error -> {
                ComingSoonScreen()
            }
        }
    }
}

@Composable
fun HeaderCartScreen(navigateBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 16.dp)
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
            "Checkout",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = modifier
                .weight(1f)
        )

        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(R.drawable.menu),
                contentDescription = null,
                modifier = modifier
                    .border(
                        1.dp,
                        color = colorResource(R.color.paint_03),
                        shape = CircleShape
                    )
                    .padding(16.dp)
            )
        }

    }
}

@Composable
fun FooterCartScreen(total: Double, modifier: Modifier = Modifier) {
    Text(
        "Shipping Information",
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(top = 16.dp)
    )

    Row(modifier = modifier.padding(top = 12.dp)) {
        NormalText("Total", modifier = modifier.weight(1f))
        NormalText("$10003", fontWeight = FontWeight.SemiBold)
    }

    Row(modifier = modifier.padding(top = 8.dp)) {
        NormalText("Shipping Fee", modifier = modifier.weight(1f))
        NormalText("$0", fontWeight = FontWeight.SemiBold)
    }

    Row(modifier = modifier.padding(top = 8.dp)) {
        NormalText("Discount", modifier = modifier.weight(1f))
        NormalText("$0", fontWeight = FontWeight.SemiBold)
    }

    HorizontalDivider(
        color = colorResource(R.color.colorDFDEDE),
        modifier = modifier.padding(top = 12.dp),
        thickness = 1.dp
    )

    Row(modifier = modifier.padding(top = 12.dp, bottom = 8.dp)) {
        NormalText("Sub Total", modifier = modifier.weight(1f))
        NormalText("$10003", fontWeight = FontWeight.SemiBold)
    }

    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(colorResource(R.color.paint_01)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Pay ${if (total == 0.0) "" else "$$total"}",
            fontSize = 18.sp,
            color = colorResource(R.color.paint_04),
            modifier = modifier.padding(vertical = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview(modifier: Modifier = Modifier) {
    VShopTheme {
//        Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        CartScreen({})
//        }
    }
}