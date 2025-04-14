package com.vanard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
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
import coil.compose.AsyncImage
import com.vanard.common.util.toastMsg
import com.vanard.resources.R
import com.vanard.domain.model.Product
import com.vanard.domain.model.dummyProduct
import com.vanard.ui.theme.VShopTheme
import com.vanard.ui.theme.Yellow

@Composable
fun ShopItemContent(
    product: Product,
    onSelectedProduct: () -> Unit,
    onFavClick: () -> Unit,
    modifier: Modifier = Modifier
) {
//    Log.d("ShopItem", "ShopItemContent: ${product.image}")
    Column(
        modifier
            .width(160.dp)
            .heightIn(min = 120.dp)
            .clickable(onClick = onSelectedProduct)
    ) {
        Box {
            AsyncImage(
                model = product.image,
//                model = "https://ryusei.co.id/cdn/shop/articles/0bbcdd3148a2240345684a406e375e8f.jpg?v=1659525141&width=2048",
                contentDescription = "Image Product",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            )
//            Image(
//                painter = painterResource(R.drawable.product1),
//                contentDescription = "Image Product",
//                contentScale = ContentScale.Crop,
//                modifier = modifier
//                    .heightIn(max = 210.dp)
//                    .fillMaxWidth()
//                    .aspectRatio(0.7f)
//                    .clip(RoundedCornerShape(16.dp))
//            )
            IconButton(
                onClick = onFavClick, modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(color = colorResource(R.color.paint_01))
                    .size(24.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.heart),
                    tint = colorResource(R.color.white),
                    contentDescription = null,
                    modifier = modifier.size(12.dp)
                )
            }
        }
        Text(
            text = product.title,
            color = colorResource(R.color.paint_05),
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            modifier = modifier.padding(top = 12.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "$${product.price}", color = colorResource(R.color.paint_05),
                fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Outlined.Star,
                tint = Yellow,
                contentDescription = null,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = "${product.rating?.rate ?: ""}",
//                modifier = Modifier.weight(0.7f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopItemPreview() {
    val context = LocalContext.current
    VShopTheme {
        ShopItemContent(
            dummyProduct,
            onSelectedProduct = { context.toastMsg("Click Product") },
            onFavClick = { context.toastMsg("Click Favorite") })
    }
}