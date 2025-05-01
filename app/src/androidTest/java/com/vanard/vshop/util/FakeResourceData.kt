package com.vanard.vshop.util

import com.vanard.domain.model.Product
import com.vanard.domain.model.Rating

val fakeProductList = listOf(
    Product(
        id = 0,
        title = "Product 1",
        price = 219.30,
        description = "Dummy Product",
        category = "Dummy",
        image = "https://blog.sribu.com/wp-content/uploads/2024/10/t-shirt-7973405_1280.jpg",
        rating = Rating(2.9, 54),
        isFavorite = false, quantityInCart = 0
    ),
    Product(
        id = 1,
        title = "Product 2",
        price = 109.30,
        description = "Dummy Product",
        category = "Dummy",
        image = "https://blog.sribu.com/wp-content/uploads/2024/10/t-shirt-7973405_1280.jpg",
        rating = Rating(3.9, 29),
        isFavorite = true, quantityInCart = 2
    ),
    Product(
        id = 2,
        title = "Product 3",
        price = 54.30,
        description = "Dummy Product",
        category = "Dummy",
        image = "https://blog.sribu.com/wp-content/uploads/2024/10/t-shirt-7973405_1280.jpg",
        rating = Rating(4.9, 7),
        isFavorite = false, quantityInCart = 1
    ),
    Product(
        id = 3,
        title = "Product 4",
        price = 88.30,
        description = "Dummy Product",
        category = "Dummy",
        image = "https://blog.sribu.com/wp-content/uploads/2024/10/t-shirt-7973405_1280.jpg",
        rating = Rating(4.9, 7),
        isFavorite = false, quantityInCart = 1
    ),
    Product(
        id = 4,
        title = "Product 5",
        price = 77.30,
        description = "Dummy Product",
        category = "Dummy",
        image = "https://blog.sribu.com/wp-content/uploads/2024/10/t-shirt-7973405_1280.jpg",
        rating = Rating(4.9, 7),
        isFavorite = false, quantityInCart = 1
    ),
    Product(
        id = 5,
        title = "Product 6",
        price = 11.30,
        description = "Dummy Product",
        category = "Dummy",
        image = "https://blog.sribu.com/wp-content/uploads/2024/10/t-shirt-7973405_1280.jpg",
        rating = Rating(4.9, 7),
        isFavorite = false, quantityInCart = 1
    ),
    Product(
        id = 6,
        title = "Product 7",
        price = 33.30,
        description = "Dummy Product",
        category = "Dummy",
        image = "https://blog.sribu.com/wp-content/uploads/2024/10/t-shirt-7973405_1280.jpg",
        rating = Rating(4.9, 7),
        isFavorite = false, quantityInCart = 1
    ),
    Product(
        id = 7,
        title = "Product 8",
        price = 22.30,
        description = "Dummy Product",
        category = "Dummy",
        image = "https://blog.sribu.com/wp-content/uploads/2024/10/t-shirt-7973405_1280.jpg",
        rating = Rating(4.9, 7),
        isFavorite = false, quantityInCart = 1
    ),
    Product(
        id = 8,
        title = "Product 9",
        price = 23.30,
        description = "Dummy Product",
        category = "Dummy",
        image = "https://blog.sribu.com/wp-content/uploads/2024/10/t-shirt-7973405_1280.jpg",
        rating = Rating(4.9, 7),
        isFavorite = false, quantityInCart = 1
    ),
    Product(
        id = 9,
        title = "Product 10",
        price = 234.30,
        description = "Dummy Product",
        category = "Dummy",
        image = "https://blog.sribu.com/wp-content/uploads/2024/10/t-shirt-7973405_1280.jpg",
        rating = Rating(4.9, 7),
        isFavorite = false, quantityInCart = 1
    ),
)

val fakeProduct = Product(
    id = 11,
    title = "Product Unk",
    price = 13.30,
    description = "Dummy Product",
    category = "Dummy",
    image = "https://blog.sribu.com/wp-content/uploads/2024/10/t-shirt-7973405_1280.jpg",
    rating = Rating(0.0, 0),
    isFavorite = false, quantityInCart = 0
)