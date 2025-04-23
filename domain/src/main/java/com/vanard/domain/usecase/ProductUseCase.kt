package com.vanard.domain.usecase

import com.vanard.common.UIState
import com.vanard.domain.model.Product
import com.vanard.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductUseCase(private val productRepository: ProductRepository) {

    suspend fun getAllProducts(): Flow<UIState<List<Product>>> {
        return productRepository.getAllProducts()
    }

    suspend fun getAllFavoriteProducts(): Flow<UIState<List<Product>>> {
        return productRepository.getAllFavoriteProducts()
    }

    suspend fun getProductById(id: Long): Flow<UIState<Product?>> {
        return productRepository.getProductById(id)
    }

    suspend fun updateProduct(product: Product) {
        productRepository.updateProduct(product)
    }

    suspend fun getAllLocalCarts(): Flow<UIState<List<Product>>> {
        return productRepository.getAllLocalCarts()
    }

    suspend fun checkCartItemExist(id: Long): Boolean {
        return productRepository.checkCartItemExist(id)
    }

    suspend fun increaseQuantityCart(id: Long) {
        productRepository.increaseQuantityCart(id)
    }

    suspend fun decreaseQuantityCart(id: Long) {
        productRepository.decreaseQuantityCart(id)
    }

}