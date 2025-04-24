package com.vanard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vanard.common.util.toastMsg
import com.vanard.domain.model.Product
import com.vanard.domain.model.dummyProduct
import com.vanard.resources.R
import com.vanard.ui.theme.VShopTheme

@Composable
fun FavoriteItemContent(
    product: Product,
    onSelectedProduct: () -> Unit,
    actions: RemoveMenuActions,
    modifier: Modifier = Modifier
) {
//    Log.d("FavoriteItemContent", "${product.image}")
    Row(
        Modifier
            .fillMaxSize()
            .clickable(onClick = onSelectedProduct)
    ) {
        ProductImage(
            product.image,
            modifier = modifier
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .size(75.dp)
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
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
                .fillMaxHeight()
        ) {
            Text(
                text = product.title,
                color = colorResource(R.color.paint_05),
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                modifier = modifier
//                    .weight(1f)
//                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.Red)
            )

            Text(
                text = "$${product.price}",
                color = colorResource(R.color.paint_05),
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }

        Box {
            IconButton(
                onClick = actions.onMenuClick, modifier = Modifier
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

            ShopDropdownMenu(actions.showMenu, actions.onDismissMenu) {
                actions.onRemoveItem()
                actions.onDismissMenu()
            }
        }
    }
}

@Composable
fun ShopDropdownMenu(showMenu: Boolean, onDismissMenu: () -> Unit, onRemoveItem: () -> Unit) {
    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = onDismissMenu
    ) {
        DropdownMenuItem(
            text = { Text("Remove") },
            onClick = onRemoveItem
//            onClick = {
//                onRemoveItem()
//                onDismissMenu()
//            }
        )
//        DropdownMenuItem(
//            text = { Text("Add to cart") },
//            onClick = {
//                expandedIndex = null
//            }
//        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteItemPreview() {
    val context = LocalContext.current
    VShopTheme {
        FavoriteItemContent(
            dummyProduct,
            onSelectedProduct = { context.toastMsg("Click Product") },
            actions = RemoveMenuActions(
                onMenuClick = { context.toastMsg("Click Product") },
                showMenu = true,
                onDismissMenu = { context.toastMsg("Click Product") },
                onRemoveItem = { context.toastMsg("Click Product") },
            ),
        )
    }
}