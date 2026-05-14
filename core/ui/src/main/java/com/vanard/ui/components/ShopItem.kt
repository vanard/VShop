package com.vanard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanard.common.util.toastMsg
import com.vanard.domain.model.Product
import com.vanard.domain.model.dummyProduct
import com.vanard.resources.R
import com.vanard.ui.theme.VShopDark
import com.vanard.ui.theme.VShopSoftSurface
import com.vanard.ui.theme.VShopSurface
import com.vanard.ui.theme.VShopTextPrimary
import com.vanard.ui.theme.VShopTextSecondary
import com.vanard.ui.theme.VShopTextTertiary
import com.vanard.ui.theme.VShopTheme

@Composable
fun ShopItemContent(
    product: Product,
    onSelectedProduct: () -> Unit,
    onFavClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeText: String? = null,
    fillWidth: Boolean = false,
) {
    val iconHeart = if (product.isFavorite) painterResource(R.drawable.heart_bold) else painterResource(R.drawable.heart)

    Column(
        modifier
            .then(if (fillWidth) Modifier.fillMaxWidth() else Modifier.width(160.dp))
            .clickable(onClick = onSelectedProduct)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(126.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(VShopSoftSurface)
        ) {
            ProductImage(
                product.image,
                modifier = Modifier
                    .matchParentSize()
                    .padding(18.dp)
            )

            badgeText?.let {
                Text(
                    text = it,
                    color = VShopSurface,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(VShopDark)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            IconButton(
                onClick = onFavClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(VShopSurface)
            ) {
                Icon(
                    painter = iconHeart,
                    tint = VShopTextSecondary,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                tint = VShopTextPrimary,
                contentDescription = null,
                modifier = Modifier.size(11.dp)
            )
            Text(
                text = " ${product.rating?.rate ?: 4.9}(${product.rating?.count ?: 2560})",
                color = VShopTextSecondary,
                fontSize = 10.sp
            )
        }

        Text(
            text = product.title,
            color = VShopTextPrimary,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = "$${product.price}",
            color = VShopTextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun FeaturedOfferCard(
    title: String,
    subtitle: String,
    action: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(108.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(VShopSoftSurface)
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            Text(text = subtitle.uppercase(), color = VShopTextTertiary, fontSize = 10.sp)
            Text(text = title, color = VShopTextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = "For your first order", color = VShopTextSecondary, fontSize = 10.sp)
            Text(
                text = action,
                color = VShopSurface,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(VShopDark)
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopItemPreview() {
    val context = LocalContext.current
    VShopTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(16.dp)) {
            ShopItemContent(
                dummyProduct,
                onSelectedProduct = { context.toastMsg("Click Product") },
                onFavClick = { context.toastMsg("Click Favorite") },
                badgeText = "Featured"
            )
        }
    }
}
