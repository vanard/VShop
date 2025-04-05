package com.vanard.vshop.data.repositoryImpl

import com.vanard.vshop.data.remote.ApiService
import com.vanard.vshop.data.remote.Product
import com.vanard.vshop.data.remote.ProductList
import com.vanard.vshop.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val productServices: ApiService) :
    ProductRepository {

    override suspend fun getAllProducts(): Response<List<Product>> {
        return productServices.getAllProducts()
    }

    override suspend fun getProductById(id: Int): Response<Product> {
        return productServices.getProductById(id)
    }

    override suspend fun getProductsByCategory(category: String): Response<List<Product>> {
        return productServices.getProductsByCategory(category)
    }

}