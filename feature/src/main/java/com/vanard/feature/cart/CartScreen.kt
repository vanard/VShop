package com.vanard.feature.cart

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.vanard.common.UIState
import com.vanard.common.util.formalDecimal
import com.vanard.domain.model.Product
import com.vanard.feature.ErrorScreen
import com.vanard.feature.base.BaseScreen
import com.vanard.resources.R
import com.vanard.ui.base.navigateBack
import com.vanard.ui.components.CartItemContent
import com.vanard.ui.components.CountViewActions
import com.vanard.ui.components.LoadingSingleTop
import com.vanard.ui.components.RemoveMenuActions
import com.vanard.ui.theme.VShopBackground
import com.vanard.ui.theme.VShopDark
import com.vanard.ui.theme.VShopSoftSurface
import com.vanard.ui.theme.VShopStroke
import com.vanard.ui.theme.VShopSurface
import com.vanard.ui.theme.VShopTextPrimary
import com.vanard.ui.theme.VShopTextSecondary
import com.vanard.ui.theme.VShopTheme

@Composable
fun CartScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = hiltViewModel()
) {
    BaseScreen(navController, requireAuth = false, showLoading = true) {
        val uiState by viewModel.uiState.collectAsState()
        val context = LocalContext.current
        val total by viewModel.total.collectAsState()
        val localCarts by viewModel.localCarts.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.getLocalCarts()
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(VShopBackground)
        ) {
            when (uiState) {
                is UIState.Loading -> LoadingSingleTop()
                is UIState.Success -> CheckoutContent(
                    context = context,
                    total = total,
                    cartList = localCarts,
                    navigateBack = { navController.navigateBack() },
                    onQuantityChanged = { product, quantity ->
                        product.quantityInCart = quantity
                        viewModel.updateCartItem(product)
                        viewModel.calculateTotal()
                    },
                    onRemove = { product ->
                        viewModel.removeCartItem(product)
                        viewModel.getLocalCarts()
                    }
                )

                is UIState.Error -> ErrorScreen()
                UIState.Idle -> Unit
            }
        }
    }
}

@Composable
private fun CheckoutContent(
    context: Context,
    total: Double,
    cartList: List<Product>,
    navigateBack: () -> Unit,
    onQuantityChanged: (Product, Int) -> Unit,
    onRemove: (Product) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { HeaderCartScreen(navigateBack = navigateBack) }
        item { CheckoutSectionTitle("ORDER SUMMARY") }
        itemsIndexed(cartList, key = { index, item -> "${item.id}-$index" }) { index, cart ->
            var showMenu by remember { mutableStateOf(false) }
            CartItemContent(
                cartItem = cart,
                onSelectedProduct = {},
                menuActions = RemoveMenuActions(
                    onMenuClick = { showMenu = true },
                    showMenu = showMenu,
                    onDismissMenu = { showMenu = false },
                    onRemoveItem = { onRemove(cart) }
                ),
                countActions = CountViewActions(
                    onPlusClick = { quantity -> onQuantityChanged(cart, quantity) },
                    onMinusClick = { quantity -> onQuantityChanged(cart, quantity) }
                )
            )
        }
        item { DeliverySection() }
        item { PaymentMethodSection() }
        item { PromoCodeSection() }
        item { OrderTotalSection(context = context, total = total, cartList = cartList) }
    }
}

@Composable
fun HeaderCartScreen(
    context: Context? = null,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(onClick = navigateBack, modifier = Modifier.size(42.dp)) {
            Icon(
                painter = painterResource(R.drawable.arrow_left_1),
                contentDescription = "Back",
                tint = VShopTextPrimary,
                modifier = Modifier
                    .border(1.dp, color = VShopStroke, shape = CircleShape)
                    .padding(9.dp)
            )
        }

        Text(
            "Check Out",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = VShopTextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.size(42.dp))
    }
}

@Composable
private fun DeliverySection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        CheckoutSectionTitle("DELIVERY TO")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(VShopSoftSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.profile_circle),
                    contentDescription = null,
                    tint = VShopTextSecondary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text("Recipient", color = VShopTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("Contact: +(62) 854-2645-1999", color = VShopTextSecondary, fontSize = 11.sp, modifier = Modifier.padding(top = 6.dp))
                Text("Address: Road 03, California, USA", color = VShopTextSecondary, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
private fun PaymentMethodSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CheckoutSectionTitle("PAYMENT METHOD")
        PaymentOption(text = "Cash on Delivery", selected = true)
        PaymentOption(text = "Digital Payment", selected = false)
    }
}

@Composable
private fun PaymentOption(text: String, selected: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .border(1.dp, VShopStroke, RoundedCornerShape(6.dp))
            .background(VShopSurface)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.shopping_bag),
            contentDescription = null,
            tint = VShopTextSecondary,
            modifier = Modifier.size(18.dp)
        )
        Text(text, color = VShopTextPrimary, fontSize = 13.sp, modifier = Modifier.padding(start = 10.dp).weight(1f))
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(selectedColor = VShopDark),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun PromoCodeSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, VShopStroke, RoundedCornerShape(8.dp))
            .background(VShopSurface)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("23KPROMOCODE", color = VShopTextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        Icon(
            painter = painterResource(R.drawable.heart_bold),
            contentDescription = null,
            tint = VShopTextPrimary,
            modifier = Modifier.padding(start = 8.dp).size(14.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text("Remove", color = VShopTextPrimary, fontSize = 12.sp)
    }
}

@Composable
private fun OrderTotalSection(context: Context, total: Double, cartList: List<Product>) {
    val deliveryFee = if (cartList.isEmpty()) 0.0 else 30.0
    val discount = if (cartList.isEmpty()) 0.0 else 300.0
    val finalTotal = (total + deliveryFee - discount).coerceAtLeast(0.0)
    val totalString = "$${finalTotal.formalDecimal()}"

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SummaryRow("Sub Total", "$${total.formalDecimal()}")
        SummaryRow("Delivery Fee", "$${deliveryFee.formalDecimal()}")
        SummaryRow("Discount", "$0.00")
        SummaryRow("Voucher Code", "-$${discount.formalDecimal()}")
        HorizontalDivider(color = VShopStroke)
        SummaryRow("Total (incl. VAT)", totalString, emphasis = true)
        Button(
            onClick = { shareOrder(context, getSummary(totalString, cartList)) },
            colors = ButtonDefaults.buttonColors(containerColor = VShopDark),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Place Order", color = VShopSurface, fontSize = 14.sp)
                Text(totalString, color = VShopSurface, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 32.dp))
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, emphasis: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            color = if (emphasis) VShopTextPrimary else VShopTextSecondary,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            value,
            color = VShopTextPrimary,
            fontSize = 14.sp,
            fontWeight = if (emphasis) FontWeight.SemiBold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun CheckoutSectionTitle(text: String) {
    Text(
        text = text,
        color = VShopTextPrimary,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    )
}

private fun getSummary(totalString: String, localCarts: List<Product>): String =
    "Total $totalString from ${localCarts.size} products"

private fun shareOrder(context: Context, summary: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Order")
        putExtra(Intent.EXTRA_TEXT, summary)
    }

    context.startActivity(Intent.createChooser(intent, "Order"))
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview(modifier: Modifier = Modifier) {
    VShopTheme {
        CartScreen(rememberNavController())
    }
}
