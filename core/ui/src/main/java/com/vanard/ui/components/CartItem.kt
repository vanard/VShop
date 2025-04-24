package com.vanard.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vanard.common.util.toastMsg
import com.vanard.domain.model.Cart
import com.vanard.domain.model.Product
import com.vanard.domain.model.dummyCart
import com.vanard.domain.model.dummyProduct
import com.vanard.resources.R
import com.vanard.ui.theme.VShopTheme

@Composable
fun CartItemContent(
    cartItem: Product,
//    cartItem: Cart,
    onSelectedProduct: () -> Unit,
    menuActions: RemoveMenuActions,
    countActions: CountViewActions,
    modifier: Modifier = Modifier
) {
    Log.d("CartItemContent", "product: $cartItem")
    Row(
        modifier
            .fillMaxWidth()
            .clickable(onClick = onSelectedProduct)
    ) {
        ProductImage(
            cartItem.image,
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .size(70.dp)
                .align(Alignment.CenterVertically)
        )

//        Image(
//            painter = painterResource(R.drawable.product1),
//            contentDescription = "Image Product",
//            contentScale = ContentScale.Crop,
//            modifier = modifier
//                .clip(RoundedCornerShape(16.dp))
//                .size(70.dp)
//                .align(Alignment.CenterVertically)
//        )

        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = cartItem.title,
//                text = "cartItem.title",
                color = colorResource(R.color.paint_05),
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                modifier = modifier
                    .padding(top = 8.dp)
            )

            Text(
                text = "$${cartItem.price}",
//                text = "$123}",
                color = colorResource(R.color.paint_05),
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        Column {
            Box(modifier = Modifier.align(Alignment.End)) {
                IconButton(
                    onClick = menuActions.onMenuClick, modifier = Modifier
                        .padding(8.dp)
                        .background(color = Color.Transparent)
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.menu1),
                        tint = colorResource(R.color.paint_05),
                        contentDescription = null,
                        modifier = modifier.size(12.dp)
                    )
                }

                ShopDropdownMenu(menuActions.showMenu, menuActions.onDismissMenu) {
                    menuActions.onRemoveItem()
                    menuActions.onDismissMenu()
                }
            }

            CountView(
                currentQuantity = cartItem.quantityInCart,
                onPlusClick = { quantity ->
                    countActions.onPlusClick(quantity)
                },
                onMinusClick = { quantity ->
                    countActions.onMinusClick(quantity)
                }
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartItemPreview() {
    val context = LocalContext.current
    VShopTheme {
        CartItemContent(
            dummyProduct,
            onSelectedProduct = { context.toastMsg("Click Product") },
            menuActions = RemoveMenuActions(
                onMenuClick = { context.toastMsg("Click Product") },
                showMenu = true,
                onDismissMenu = { context.toastMsg("Click Product") },
                onRemoveItem = { context.toastMsg("Click Product") },
            ),
            countActions = CountViewActions(
                onPlusClick = { context.toastMsg("Click Product") },
                onMinusClick = { context.toastMsg("Click Product") },
            )

        )
    }
}