package com.vanard.domain.model

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

val dummyCart = Cart(0, 123123123, "", listOf(ProductItem(123123123, 2)))