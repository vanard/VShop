package com.vanard.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vanard.domain.model.Rating

@Entity(tableName = "carts_item")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val date: String? = null,
    val title: String,
    val price: Double,
    val category: String,
    val image: String,
    val quantity: Long,
    @Embedded val rating: RatingEntity,
    val description: String,
    var isFavorite: Boolean,
)
