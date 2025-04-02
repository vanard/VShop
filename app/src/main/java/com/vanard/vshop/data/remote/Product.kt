package com.vanard.vshop.data.remote

data class ProductList(val products: List<Product>)

data class Product(
    val id: Long,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating? = null,
)

data class Rating(
    val rate: Double,
    val count: Long,
)
