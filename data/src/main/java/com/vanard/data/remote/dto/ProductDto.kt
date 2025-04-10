package com.vanard.data.remote.dto

data class ProductListDto(val products: List<ProductDto>)

data class ProductDto(
    val id: Long,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: RatingDto? = null,
)

data class RatingDto(
    val rate: Double,
    val count: Long,
)
