package com.vanard.domain.model

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

val dummyProduct = Product(
    0,
    "Title Product",
    219.30,
    "Dummy Product",
    "Dummy",
    image = "https://blog.sribu.com/wp-content/uploads/2024/10/t-shirt-7973405_1280.jpg",
    rating = Rating(2.9, 14)
)
