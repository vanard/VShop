package com.vanard.data.repositoryImpl

import com.vanard.common.UIState
import com.vanard.common.util.networkBoundResourceUiState
import com.vanard.core.common.asUiState
import com.vanard.core.common.fetchState
import com.vanard.core.common.map
import com.vanard.core.common.safeBodyOrThrow
import com.vanard.data.dao.ProductDao
import com.vanard.data.mappers.toDomain
import com.vanard.data.mappers.toEntity
import com.vanard.data.remote.ProductService
import com.vanard.domain.model.Product
import com.vanard.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productServices: ProductService,
    private val productDao: ProductDao
) :
    ProductRepository {

    override suspend fun getAllProducts(): Flow<UIState<List<Product>>> =
        networkBoundResourceUiState(
            query = {
                productDao.getAllProducts().map { list -> list.map { it.toDomain() } }
            },
            fetch = {
                productServices.getAllProducts().safeBodyOrThrow()
            },
            saveFetchResult = { dtoList ->
//                productDao.deleteAllProducts()
                productDao.insertAllProduct(dtoList.map { it.toEntity() })
            },
            shouldFetch = { localData ->
                localData.isEmpty()
            }
        )

//    override suspend fun getProductById(id: Long): Flow<UIState<Product?>> = flow {
//        val result = fetchState { productServices.getProductById(id) }
//        emit(result.map { dto ->
//            dto.toDomain()
//        })
//    }

    override suspend fun getProductById(id: Long): Flow<UIState<Product?>> =
        networkBoundResourceUiState(
            query = {
                productDao.getProductById(id).map { product -> product?.toDomain() }
            },
            fetch = {
                productServices.getProductById(id).safeBodyOrThrow()
            },
            saveFetchResult = { productDto ->
                productDao.insertProduct(productDto.toEntity())
            },
            shouldFetch = { localData ->
                localData == null
            }
        )

    override suspend fun getProductsByCategory(category: String): Flow<UIState<List<Product>>> =
        flow {
            val result = fetchState { productServices.getProductsByCategory(category) }
            emit(result.map { dtoList ->
                dtoList.map { it.toDomain() }
            })
        }

    override suspend fun getAllFavoriteProducts(): Flow<UIState<List<Product>>> {
        val flowProduct = productDao.getAllFavoriteProducts().map { list ->
            list.map { it.toDomain() }
        }.asUiState()

        return flowProduct
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product.toEntity())
    }

    override suspend fun getAllLocalCarts(): Flow<UIState<List<Product>>> {
        val flowProduct = productDao.getAllProductInChart().map { list ->
            list.map { it.toDomain() }
        }.asUiState()

        return flowProduct
    }

    override suspend fun checkCartItemExist(id: Long): Boolean {
        return productDao.getProductInChartExist(id)
    }

    override suspend fun increaseQuantityCart(id: Long) {
        productDao.increaseQuantity(id)
    }

    override suspend fun decreaseQuantityCart(id: Long) {
        productDao.decreaseQuantity(id)
    }

}