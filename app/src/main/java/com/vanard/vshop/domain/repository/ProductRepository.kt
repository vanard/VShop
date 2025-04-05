package com.vanard.vshop.domain.repository

import com.vanard.vshop.data.remote.Product
import com.vanard.vshop.data.remote.ProductList
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ProductRepository {
    suspend fun getAllProducts(): Response<List<Product>>
    suspend fun getProductById(id: Int): Response<Product>
    suspend fun getProductsByCategory(category: String): Response<List<Product>>
}