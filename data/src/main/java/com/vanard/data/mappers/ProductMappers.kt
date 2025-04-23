package com.vanard.data.mappers

import com.vanard.data.entities.ProductEntity
import com.vanard.data.entities.RatingEntity
import com.vanard.data.remote.dto.ProductDto
import com.vanard.data.remote.dto.RatingDto
import com.vanard.domain.model.Product
import com.vanard.domain.model.Rating

fun ProductDto.toDomain(): Product {
    return Product(
        id = this.id,
        title = this.title,
        price = this.price,
        description = this.description,
        category = this.category,
        image = this.image,
        rating = this.rating?.toDomain(),
        isFavorite = false,
        quantityInCart = 0
    )
}

fun RatingDto.toDomain(): Rating {
    return Rating(this.rate, this.count)
}

//

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        title = this.title,
        price = this.price,
        description = this.description,
        category = this.category,
        image = this.image,
        rating = this.rating?.toEntity(),
        isFavorite = false,
        quantityInCart = 0
    )
}

fun RatingDto.toEntity(): RatingEntity {
    return RatingEntity(this.rate, this.count)
}

//

fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        title = this.title,
        price = this.price,
        description = this.description,
        category = this.category,
        image = this.image,
        rating = this.rating?.toDomain(),
        isFavorite = this.isFavorite,
        quantityInCart = quantityInCart
    )
}

fun RatingEntity.toDomain(): Rating {
    return Rating(this.rate, this.count)
}

//
fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = this.id,
        title = this.title,
        price = this.price,
        description = this.description,
        category = this.category,
        image = this.image,
        rating = this.rating?.toEntity(),
        isFavorite = this.isFavorite,
        quantityInCart = this.quantityInCart
    )
}

fun Rating.toEntity(): RatingEntity {
    return RatingEntity(this.rate, this.count)
}

