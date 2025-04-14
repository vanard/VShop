package com.vanard.feature.cart

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vanard.resources.R
import com.vanard.ui.theme.VShopTheme

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel()
) {
    var total = 0
    viewModel.getCarts()

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

        LazyColumn(modifier = modifier) {

        }

        Text("Shipping Information", fontWeight = FontWeight.SemiBold)
        Row(modifier = modifier.padding(top = 16.dp)) {
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
        HorizontalDivider(modifier = modifier.padding(top = 16.dp), thickness = 1.dp)
        Row(modifier = modifier.padding(top = 16.dp)) {
            Text("Sub Total", modifier = modifier.weight(1f))
            Text("$10003", fontWeight = FontWeight.SemiBold)
        }

        Button(
            onClick = {}, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
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
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    VShopTheme {
//        Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        CartScreen()
//        }
    }
}