package com.vanard.feature.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.draw.clip
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
import com.vanard.common.UIState
import com.vanard.common.util.firstWords
import com.vanard.common.util.toastMsg
import com.vanard.feature.ErrorScreen
import com.vanard.resources.R
import com.vanard.ui.components.ExpandableText
import com.vanard.ui.components.LoadingSingleTop
import com.vanard.ui.components.NormalText
import com.vanard.ui.components.ProductImage
import com.vanard.ui.theme.VShopTheme
import com.vanard.ui.theme.Yellow

object DetailScreenTestTag {
    const val FAV = "favorite"
    const val ADD_CART = "add_cart"
    const val ARROW_BACK = "arrow_back"
    const val READ_MORE = "read_more"
}

@Composable
fun DetailScreen(
    productId: Long,
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {

    viewModel.uiState.collectAsState().value.let { uiState ->
        when (uiState) {
            is UIState.Loading -> {
                viewModel.getProductById(productId)
                LoadingSingleTop()
            }

            is UIState.Error -> ErrorScreen()
            is UIState.Success -> {
                val context = LocalContext.current
                val item by viewModel.product.collectAsState()

                item?.let { product ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            ProductImage(
                                product.image,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .heightIn(min = 200.dp, max = 400.dp)
                            )
//                            Image(
//                                painter = painterResource(R.drawable.product1),
//                                contentScale = ContentScale.Crop,
//                                contentDescription = "Product Image",
//                                modifier = Modifier.clip(
//                                    RoundedCornerShape(16.dp)
//                                )
//                            )

                            IconButton(
                                onClick = {
                                    navController.navigateUp()
                                },
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(16.dp)
                                    .clip(CircleShape)
                                    .background(color = colorResource(R.color.paint_04))
                                    .size(48.dp)
                                    .testTag(DetailScreenTestTag.ARROW_BACK)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.arrow_left_1),
                                    tint = colorResource(R.color.paint_05),
                                    contentDescription = "Back Icon",
                                    modifier = modifier.size(32.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    val message = if (product.isFavorite)
                                        "${product.title.firstWords(2)} has been removed from your favorites."
                                    else
                                        "${product.title.firstWords(2)} has been added to your favorites."

                                    context.toastMsg(message)
                                    viewModel.loveProduct(product)
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                                    .clip(CircleShape)
                                    .background(color = colorResource(R.color.paint_04))
                                    .size(48.dp)
                                    .testTag(DetailScreenTestTag.FAV)
                            ) {
                                Icon(
                                    painter = if (product.isFavorite) painterResource(R.drawable.heart_bold)
                                    else painterResource(R.drawable.heart),
                                    tint = colorResource(R.color.paint_05),
                                    contentDescription = null,
                                    modifier = modifier.size(36.dp)
                                )
                            }
                        }

//                        Row(modifier = Modifier.fillMaxWidth()) {
//                            Column(modifier = Modifier.weight(1f)) {
//                                Text(
//                                    product.title,
//                                    fontWeight = FontWeight.SemiBold,
//                                    fontSize = 22.sp
//                                )
//
//                                Row(modifier = Modifier.padding(top = 8.dp)) {
//                                    Icon(
//                                        imageVector = Icons.Default.Star,
//                                        tint = Yellow,
//                                        contentDescription = "Star Icon",
//                                        modifier = modifier
//                                    )
//                                    Spacer(Modifier.width(8.dp))
//                                    Text("${product.rating?.rate}")
//                                    Spacer(Modifier.width(6.dp))
//                                    Text("(${product.rating?.count} reviews)")
//                                }
//                            }
//
//                            CountView(onPlusClick = {}, onMinusClick = {}, customIconSize = 32)
//                        }

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                product.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 22.sp
                            )

                            Row(modifier = Modifier.padding(top = 8.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    tint = Yellow,
                                    contentDescription = "Star Icon",
                                    modifier = modifier
                                )
                                Spacer(Modifier.width(8.dp))
                                NormalText("${product.rating?.rate}")
                                Spacer(Modifier.width(6.dp))
                                NormalText("(${product.rating?.count} reviews)")
                            }
                        }

                        ExpandableText(
                            product.description,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 8.dp)
                                .testTag(DetailScreenTestTag.READ_MORE)
                        )

                        HorizontalDivider(
                            color = colorResource(R.color.colorDFDEDE),
                            modifier = modifier.padding(vertical = 12.dp),
                            thickness = 1.dp
                        )

                        Button(
                            onClick = {
                                context.toastMsg("Product added")
                                viewModel.addToCart(product)
                            },
                            colors = ButtonDefaults.buttonColors(colorResource(R.color.paint_01)),
                            modifier = Modifier.fillMaxWidth().testTag(DetailScreenTestTag.ADD_CART)
                        ) {
                            Row(modifier = Modifier.padding(vertical = 16.dp)) {
                                Icon(
                                    painter = painterResource(R.drawable.shopping_cart),
                                    contentDescription = "Shop Button"
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Add to Cart | $${product.price}")
                            }
                        }
                    }
                }

            }

            UIState.Idle -> TODO()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    VShopTheme {
        DetailScreen(0, rememberNavController())
    }
}