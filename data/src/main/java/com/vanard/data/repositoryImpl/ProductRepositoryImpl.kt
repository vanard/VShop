package com.vanard.data.repositoryImpl

import com.vanard.core.common.UIState
import com.vanard.core.common.fetchState
import com.vanard.core.common.map
import com.vanard.data.mappers.toDomain
import com.vanard.data.remote.ApiService
import com.vanard.domain.model.Product
import com.vanard.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val productServices: ApiService) :
    ProductRepository {

    override suspend fun getAllProducts(): Flow<UIState<List<Product>>> = flow {
        val result = fetchState { productServices.getAllProducts() }
        emit(result.map { dtoList ->
            dtoList.map { it.toDomain() }
        })
    }

    override suspend fun getProductById(id: Int): Flow<UIState<Product>> = flow {
        val result = fetchState { productServices.getProductById(id) }
        emit(result.map{ dto ->
            dto.toDomain()
        })
    }

    override suspend fun getProductsByCategory(category: String): Flow<UIState<List<Product>>> = flow {
        val result = fetchState { productServices.getProductsByCategory(category) }
        emit(result.map { dtoList ->
            dtoList.map { it.toDomain() }
        })
    }

}