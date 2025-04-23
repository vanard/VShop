package com.vanard.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "products_item")
data class ProductEntity(
    @PrimaryKey val id: Long,
//    val uuid: String = UUID.randomUUID().toString(),
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