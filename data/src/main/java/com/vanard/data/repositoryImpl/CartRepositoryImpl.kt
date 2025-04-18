package com.vanard.data.repositoryImpl

import com.vanard.common.UIState
import com.vanard.core.common.asUiState
import com.vanard.core.common.fetchState
import com.vanard.core.common.map
import com.vanard.data.dao.CartDao
import com.vanard.data.mappers.toDomain
import com.vanard.data.remote.CartService
import com.vanard.domain.model.Cart
import com.vanard.domain.model.CartItem
import com.vanard.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.collections.map

class CartRepositoryImpl @Inject constructor(private val cartServices: CartService, private val cartDao: CartDao) :
    CartRepository {

    override suspend fun getAllCarts(): Flow<UIState<List<Cart>>> = flow {
        val result = fetchState { cartServices.getAllCarts() }
        emit(result.map { dtoList ->
            dtoList.map { it.toDomain() }
        })
    }

    override suspend fun getAllLocalCarts(): Flow<UIState<List<CartItem>>> =
        cartDao.getAllCarts().map { list ->
            list.map { it.toDomain() }
        }.asUiState()


}