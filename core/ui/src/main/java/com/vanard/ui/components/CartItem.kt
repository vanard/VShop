package com.vanard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanard.common.util.toastMsg
import com.vanard.domain.model.Product
import com.vanard.domain.model.dummyProduct
import com.vanard.ui.theme.VShopSoftSurface
import com.vanard.ui.theme.VShopTextPrimary
import com.vanard.ui.theme.VShopTextSecondary
import com.vanard.ui.theme.VShopTheme

@Composable
fun CartItemContent(
    cartItem: Product,
    onSelectedProduct: () -> Unit,
    menuActions: RemoveMenuActions,
    countActions: CountViewActions,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .clickable(onClick = onSelectedProduct)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProductImage(
            cartItem.image,
            modifier = Modifier
                .size(84.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(VShopSoftSurface)
                .padding(14.dp)
        )

        Column(
            modifier = Modifier
                .padding(start = 14.dp)
                .weight(1f)
        ) {
            Text(
                text = cartItem.title,
                color = VShopTextPrimary,
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = cartItem.category,
                color = VShopTextSecondary,
                fontSize = 11.sp,
                maxLines = 1,
                modifier = Modifier.padding(top = 4.dp),
            )
            Text(
                text = "$${cartItem.price}",
                color = VShopTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 10.dp),
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Box {
                ShopDropdownMenu(menuActions.showMenu, menuActions.onDismissMenu) {
                    menuActions.onRemoveItem()
                    menuActions.onDismissMenu()
                }
            }
            CountView(
                currentQuantity = cartItem.quantityInCart.coerceAtLeast(1),
                onPlusClick = countActions.onPlusClick,
                onMinusClick = countActions.onMinusClick,
                customIconSize = 26
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
                onMenuClick = { context.toastMsg("Click Menu") },
                showMenu = false,
                onDismissMenu = {},
                onRemoveItem = {},
            ),
            countActions = CountViewActions(
                onPlusClick = {},
                onMinusClick = {},
            )
        )
    }
}
