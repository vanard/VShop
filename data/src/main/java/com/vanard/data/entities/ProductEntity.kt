package com.vanard.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products_item")
data class ProductEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    @Embedded val rating: RatingEntity?,
    var isFavorite: Boolean,
    var quantityInCart: Int
)

data class RatingEntity(
    val rate: Double,
    val count: Long,
)