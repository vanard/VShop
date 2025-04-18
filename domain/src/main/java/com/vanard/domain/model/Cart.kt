package com.vanard.domain.model

import com.vanard.common.util.toIso8601Utc

data class CartList(val carts: List<Cart>)

data class Cart(
    val id: Long,
    val userId: Long,
    val date: String? = null,
    val products: List<ProductItem>,
)

data class ProductItem(
    val productId: Long,
    val quantity: Long,
)

data class CartItem(
    val id: Long,
    val date: String? = null,
    val title: String,
    val price: Double,
    val category: String,
    val image: String,
    val quantity: Long = 1,
)

val dummyCart = Cart(0, 123123123, "", listOf(ProductItem(123123123, 2)))
val dummyCartItem = CartItem(
    0,
    System.currentTimeMillis().toIso8601Utc(),
    "Tas Banteng",
    123123123.0,
    "women",
    "https://contents.mediadecathlon.com/p2691013/k\$161820d35a9a96f968f2d592788cf8f6/tas-ransel-hiking-anak-remaja-mh300-15l-pink-quechua-8739843.jpg?f=1920x0&format=auto",
    1
)