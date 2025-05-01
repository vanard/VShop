package com.vanard.vshop.repository

import com.vanard.common.UIState
import com.vanard.common.util.networkBoundResourceUiState
import com.vanard.domain.model.Product
import com.vanard.domain.model.dummyProduct
import com.vanard.domain.repository.ProductRepository
import com.vanard.vshop.util.fakeProduct
import com.vanard.vshop.util.fakeProductList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeSuccessRepositoryImpl : ProductRepository {
    private val dbFlow = MutableStateFlow(listOf<Product>())
    private val list = mutableListOf<Product>()

    fun reset() {
        list.clear()
    }

    private fun decreaseQuantity(id: Long) {
        list.find { it.id == id }?.let { product ->
            if (product.quantityInCart > 1)
                product.apply {
                    quantityInCart--
                }
        }
        dbFlow.value = list.toList()
    }

    private fun increaseQuantity(id: Long) {
        list.find { it.id == id }?.let { product ->
            product.apply {
                quantityInCart++
            }
        }
        dbFlow.value = list.toList()
    }

    override suspend fun getAllProducts(): Flow<UIState<List<Product>>> =
        networkBoundResourceUiState(
            query = {
                flowOf(dbFlow.value)
            },
            fetch = {
                fakeProductList
            },
            saveFetchResult = { dtoList ->
                list.addAll(dtoList)
                dbFlow.value = list.toList()
            },
            shouldFetch = { localData ->
                dbFlow.value.isEmpty()
            }
        )

    override suspend fun getProductById(id: Long): Flow<UIState<Product?>> = flow {
        emit(
            dbFlow.value.find { it.id == id }?.let { product ->
                UIState.Success(product)
            } ?: run { UIState.Success(fakeProduct) }
        )
    }

    override suspend fun getProductsByCategory(category: String): Flow<UIState<List<Product>>> =
        flow {
            emit(UIState.Success(listOf(dummyProduct)))
        }

    override suspend fun getAllFavoriteProducts(): Flow<UIState<List<Product>>> {
        val flowProduct = flowOf(UIState.Success(dbFlow.value.filter { it.isFavorite }))

        return flowProduct
    }

    override suspend fun updateProduct(product: Product) {
        list.find { it.id == product.id }?.let { dbProduct ->
            dbProduct.apply {
                this.isFavorite = product.isFavorite
                this.quantityInCart = product.quantityInCart
            }
        }
        dbFlow.value = list.toList()
    }

    override suspend fun getAllLocalCarts(): Flow<UIState<List<Product>>> {
        val flowProduct = flowOf(UIState.Success(dbFlow.value.filter { it.quantityInCart > 0 }))

        return flowProduct
    }

    override suspend fun checkCartItemExist(id: Long): Boolean {
        return dbFlow.value.any { it.id == id }
    }

    override suspend fun increaseQuantityCart(id: Long) {
        increaseQuantity(id)
    }

    override suspend fun decreaseQuantityCart(id: Long) {
        decreaseQuantity(id)
    }
}

class FakeFailureRepositoryImpl : ProductRepository {
    private val dbFlow = MutableStateFlow(listOf<Product>())
    private val list = mutableListOf<Product>()

    companion object {
        val errorMessage = "error message"
    }

    fun reset() {
        list.clear()
    }

    private fun decreaseQuantity(id: Long) {
        list.find { it.id == id }?.let { product ->
            if (product.quantityInCart > 1)
                product.apply {
                    quantityInCart--
                }
        }
        dbFlow.value = list.toList()
    }

    private fun increaseQuantity(id: Long) {
        list.find { it.id == id }?.let { product ->
            product.apply {
                quantityInCart++
            }
        }
        dbFlow.value = list.toList()
    }

    override suspend fun getAllProducts(): Flow<UIState<List<Product>>> =
        networkBoundResourceUiState(
            query = {
                flowOf(dbFlow.value)
            },
            fetch = {
                null
            },
            saveFetchResult = { dtoList ->
                list.addAll(emptyList())
                dbFlow.value = list.toList()
            },
            shouldFetch = { localData ->
                dbFlow.value.isEmpty()
            }
        )

    override suspend fun getProductById(id: Long): Flow<UIState<Product?>> = flow {
        emit(
            dbFlow.value.find { it.id == id }?.let { product ->
                UIState.Error(errorMessage)
            } ?: run { UIState.Error(errorMessage) }
        )
    }

    override suspend fun getProductsByCategory(category: String): Flow<UIState<List<Product>>> =
        flow {
            emit(UIState.Error(errorMessage))
        }

    override suspend fun getAllFavoriteProducts(): Flow<UIState<List<Product>>> {
        val flowProduct = flowOf(UIState.Error(errorMessage))

        return flowProduct
    }

    override suspend fun updateProduct(product: Product) {
        list.find { it.id == product.id }?.let { dbProduct ->
            dbProduct.apply {
                this.isFavorite = product.isFavorite
                this.quantityInCart = product.quantityInCart
            }
        }
        dbFlow.value = list.toList()
    }

    override suspend fun getAllLocalCarts(): Flow<UIState<List<Product>>> {
        val flowProduct = flowOf(UIState.Error(errorMessage))

        return flowProduct
    }

    override suspend fun checkCartItemExist(id: Long): Boolean {
        return dbFlow.value.any { it.id == id }
    }

    override suspend fun increaseQuantityCart(id: Long) {
        increaseQuantity(id)
    }

    override suspend fun decreaseQuantityCart(id: Long) {
        decreaseQuantity(id)
    }
}