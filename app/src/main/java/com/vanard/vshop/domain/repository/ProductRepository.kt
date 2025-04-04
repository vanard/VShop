package com.vanard.vshop.domain.repository

import com.vanard.vshop.data.remote.Product
import com.vanard.vshop.data.remote.ProductList
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ProductRepository {
    suspend fun getAllProducts(): Response<ProductList>
    suspend fun getProductById(id: String): Response<Product>
    suspend fun getProductsByCategory(category: String): Response<ProductList>
}