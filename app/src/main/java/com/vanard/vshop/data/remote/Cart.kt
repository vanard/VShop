package com.vanard.vshop.data.remote

data class CartList(val carts: List<Cart>)

data class Cart(
    val id: Long,
    val userId: Long,
    val date: String? = null,
    val products: List<ProductItem>,
//    @JsonProperty("__v")
//    val v: Long,
    val __v: Long? = null,
)

data class ProductItem(
    val productId: Long,
    val quantity: Long,
)