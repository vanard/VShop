package com.vanard.feature.cart

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
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
import com.vanard.common.util.toastMsg
import com.vanard.resources.R
import com.vanard.ui.components.CartItemContent
import com.vanard.ui.theme.VShopTheme

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel()
) {
    var total = 0
    viewModel.getCarts()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val context = LocalContext.current
    val carts by viewModel.carts.collectAsState()
    val scrollState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(vertical = 16.dp)
        ) {
            IconButton(onClick = {}) {
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

        LazyColumn(
            state = scrollState,
            modifier = modifier.sizeIn(maxHeight = (screenHeight / 2) - 48.dp),
            content = {
                items(
                    items = carts.carts,
                    key = { it.id }) { cart ->
                    CartItemContent(
                        cart,
                        onSelectedProduct = {
                            context.toastMsg("Product Cart")
                        },
                    )
                }

            })

        Text(
            "Shipping Information",
            fontWeight = FontWeight.SemiBold,
            modifier = modifier.padding(top = 16.dp)
        )

        Row(modifier = modifier.padding(top = 12.dp)) {
            Text("Total", modifier = modifier.weight(1f))
            Text("$10003", fontWeight = FontWeight.SemiBold)
        }

        Row(modifier = modifier.padding(top = 8.dp)) {
            Text("Shipping Fee", modifier = modifier.weight(1f))
            Text("$0", fontWeight = FontWeight.SemiBold)
        }

        Row(modifier = modifier.padding(top = 8.dp)) {
            Text("Discount", modifier = modifier.weight(1f))
            Text("$0", fontWeight = FontWeight.SemiBold)
        }

        HorizontalDivider(
            color = colorResource(R.color.paint_04),
            modifier = modifier.padding(top = 16.dp),
            thickness = 1.dp
        )

        Row(modifier = modifier.padding(top = 16.dp)) {
            Text("Sub Total", modifier = modifier.weight(1f))
            Text("$10003", fontWeight = FontWeight.SemiBold)
        }

        Button(
            onClick = {}, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Pay ${if (total == 0) "" else "$$total"}",
                fontSize = 18.sp,
                modifier = modifier.padding(vertical = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview(modifier: Modifier = Modifier) {
    VShopTheme {
//        Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        CartScreen()
//        }
    }
}