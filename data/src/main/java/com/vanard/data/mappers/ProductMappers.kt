package com.vanard.data.mappers

import com.vanard.data.remote.dto.ProductDto
import com.vanard.data.remote.dto.RatingDto
import com.vanard.domain.model.Product
import com.vanard.domain.model.Rating

fun ProductDto.toDomain(): Product {
    return Product(
        this.id,
        this.title,
        this.price,
        this.description,
        this.category,
        this.image,
        this.rating?.toDomain()
    )
}

fun RatingDto.toDomain(): Rating {
    return Rating(this.rate, this.count)
}

