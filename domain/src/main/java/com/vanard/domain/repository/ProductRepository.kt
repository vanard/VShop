package com.vanard.domain.repository

import com.vanard.common.UIState
import com.vanard.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllProducts(): Flow<UIState<List<Product>>>
    suspend fun getProductById(id: Int): Flow<UIState<Product>>
    suspend fun getProductsByCategory(category: String): Flow<UIState<List<Product>>>
}