package com.vanard.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.vanard.domain.model.dummyCart
import com.vanard.resources.R
import com.vanard.ui.theme.VShopTheme

@Composable
fun CartItemContent(
    product: Cart,
    onSelectedProduct: () -> Unit,
    modifier: Modifier = Modifier
) {
//    Log.d("ShopItem", "ShopItemContent: ${product.image}")
    Row(
        modifier
            .fillMaxWidth()
            .clickable(onClick = onSelectedProduct)
    ) {
        //            AsyncImage(
//                model = product.image,
////                model = "https://ryusei.co.id/cdn/shop/articles/0bbcdd3148a2240345684a406e375e8f.jpg?v=1659525141&width=2048",
//                contentDescription = "Image Product",
//                contentScale = ContentScale.Crop,
//                modifier = modifier
//                    .clip(RoundedCornerShape(16.dp))
//                    .size(70.dp)
//            )
        Image(
            painter = painterResource(R.drawable.product1),
            contentDescription = "Image Product",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .clip(RoundedCornerShape(16.dp))
                .size(70.dp)
                .align(Alignment.CenterVertically)
        )

        Column(
            modifier = modifier
                .padding(horizontal = 8.dp)
                .weight(1f)
        ) {
            Text(
//                text = product.title,
                text = "product.title",
                color = colorResource(R.color.paint_05),
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                modifier = modifier
                    .padding(top = 8.dp)
            )

            Text(
//                text = "$${product.price}",
                text = "$123}",
                color = colorResource(R.color.paint_05),
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        Column {
            IconButton(
                onClick = onSelectedProduct, modifier = Modifier
                    .padding(8.dp)
                    .background(color = Color.Transparent)
                    .size(24.dp)
                    .align(Alignment.End)
            ) {
                Icon(
                    painter = painterResource(R.drawable.menu1),
                    tint = colorResource(R.color.paint_05),
                    contentDescription = null,
                    modifier = modifier.size(12.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                IconButton(
                    onClick = onSelectedProduct, modifier = Modifier
                        .padding(8.dp)
                        .border(1.dp, color = colorResource(R.color.paint_03), shape = CircleShape)
                        .clip(CircleShape)
                        .background(color = Color.Transparent)
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.minus),
                        tint = colorResource(R.color.paint_05),
                        contentDescription = null,
                        modifier = modifier.size(12.dp)
                    )
                }

                Text("4", modifier = modifier.padding(start = 4.dp))

                IconButton(
                    onClick = onSelectedProduct, modifier = Modifier
                        .padding(8.dp)
                        .border(1.dp, color = colorResource(R.color.paint_03), shape = CircleShape)
                        .clip(CircleShape)
                        .background(color = Color.Transparent)
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add),
                        tint = colorResource(R.color.paint_05),
                        contentDescription = null,
                        modifier = modifier.size(12.dp)
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartItemPreview() {
    val context = LocalContext.current
    VShopTheme {
        CartItemContent(
            dummyCart,
            onSelectedProduct = { context.toastMsg("Click Product") },
        )
    }
}