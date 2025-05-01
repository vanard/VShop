package com.vanard.vshop.repository

import com.vanard.common.UIState
import com.vanard.common.util.networkBoundResourceUiState
import com.vanard.core.common.asUiState
import com.vanard.data.dao.ProductDao
import com.vanard.data.mappers.toDomain
import com.vanard.data.mappers.toEntity
import com.vanard.domain.model.Product
import com.vanard.domain.repository.ProductRepository
import com.vanard.vshop.util.fakeProduct
import com.vanard.vshop.util.fakeProductList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FakeProductRepository @Inject constructor(
    private val productDao: ProductDao
) :
    ProductRepository {

    override suspend fun getAllProducts(): Flow<UIState<List<Product>>> =
        networkBoundResourceUiState(
            query = {
                productDao.getAllProducts().map { list -> list.map { it.toDomain() } }
            },
            fetch = {
                fakeProductList
            },
            saveFetchResult = { dtoList ->
                productDao.insertAllProduct(dtoList.map { it.toEntity() })
            },
            shouldFetch = { localData ->
                localData.isEmpty()
            }
        )

    override suspend fun getProductById(id: Long): Flow<UIState<Product?>> =
        networkBoundResourceUiState(
            query = {
                productDao.getProductById(id).map { product -> product?.toDomain() }
            },
            fetch = {
                fakeProductList.find { it.id == id } ?: fakeProduct
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
            emit(UIState.Success(fakeProductList))
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