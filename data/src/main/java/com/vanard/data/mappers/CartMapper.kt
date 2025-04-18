package com.vanard.data.mappers

import com.vanard.data.entities.CartEntity
import com.vanard.data.remote.dto.CartDto
import com.vanard.data.remote.dto.ProductItemDto
import com.vanard.domain.model.Cart
import com.vanard.domain.model.CartItem
import com.vanard.domain.model.ProductItem

fun CartDto.toDomain(): Cart {
    return Cart(
        this.id,
        this.userId,
        this.date,
        this.products.map { it.toDomain() }
    )
}

fun ProductItemDto.toDomain(): ProductItem {
    return ProductItem(this.productId, this.quantity)
}

//
fun CartEntity.toDomain(): CartItem {
    return CartItem(
        this.id,
        this.date,
        this.title,
        this.price,
        this.category,
        this.image,
        this.quantity
    )
}