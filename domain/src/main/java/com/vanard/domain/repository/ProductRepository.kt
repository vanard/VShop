package com.vanard.domain.repository

import com.vanard.common.UIState
import com.vanard.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllProducts(): Flow<UIState<List<Product>>>
    suspend fun getProductById(id: Long): Flow<UIState<Product?>>
    suspend fun getProductsByCategory(category: String): Flow<UIState<List<Product>>>

    suspend fun getAllFavoriteProducts(): Flow<UIState<List<Product>>>
    suspend fun updateProduct(product: Product)

    suspend fun getAllLocalCarts(): Flow<UIState<List<Product>>>
    suspend fun checkCartItemExist(id: Long): Boolean
    suspend fun increaseQuantityCart(id: Long)
    suspend fun decreaseQuantityCart(id: Long)
}