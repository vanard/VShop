package com.vanard.domain.usecase

import com.vanard.common.UIState
import com.vanard.domain.model.Product
import com.vanard.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class HomeUseCase (private val productRepository: ProductRepository) {

    suspend fun getAllProducts(): Flow<UIState<List<Product>>> {
        return productRepository.getAllProducts()
    }

}