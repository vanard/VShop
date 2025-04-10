package com.vanard.data.remote.dto

data class CartListDto(val carts: List<CartDto>)

data class CartDto(
    val id: Long,
    val userId: Long,
    val date: String? = null,
    val products: List<ProductItemDto>,
//    @JsonProperty("__v")
//    val v: Long,
    val __v: Long? = null,
)

data class ProductItemDto(
    val productId: Long,
    val quantity: Long,
)