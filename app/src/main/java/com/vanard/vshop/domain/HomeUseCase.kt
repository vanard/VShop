package com.vanard.vshop.domain

import com.vanard.vshop.common.UIState
import com.vanard.vshop.common.fetchState
import com.vanard.vshop.data.remote.Product
import com.vanard.vshop.data.remote.ProductList
import com.vanard.vshop.domain.repository.ProductRepository
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val productRepository: ProductRepository) {

//    suspend fun getAllProducts(): UIState<ProductList> {
//        return fetchState { productRepository.getAllProducts() }
//    }

    suspend fun getAllProducts(): List<Product> {
        return productRepository.getAllProducts().body() ?: listOf()
    }

}