package com.vanard.data.repositoryImpl

import com.vanard.common.UIState
import com.vanard.common.util.fetchState
import com.vanard.common.util.map
import com.vanard.data.dao.CartDao
import com.vanard.data.mappers.toDomain
import com.vanard.data.remote.CartService
import com.vanard.domain.model.Cart
import com.vanard.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val cartServices: CartService, private val cartDao: CartDao) :
    CartRepository {

    override suspend fun getAllCarts(): Flow<UIState<List<Cart>>> = flow {
        val result = fetchState { cartServices.getAllCarts() }
        emit(result.map { dtoList ->
            dtoList.map { it.toDomain() }
        })
    }

//    override suspend fun getAllLocalCarts(): Flow<UIState<List<CartItem>>> =
//        cartDao.getAllCarts().map { list ->
//            list.map { it.toDomain() }
//        }.asUiState()
//
//    override suspend fun getCartItemByUUID(uuid: String): Flow<UIState<List<CartItem>>> {
//        cartDao.getCartById()
//    }
//
//    override suspend fun addToCart(product: CartItem) {
//        cartDao.insertCart(product.toEntity())
//    }
//
//    override suspend fun updateCartItem(product: CartItem) {
//        cartDao.updateCartItem(product.toEntity())
//    }
//
//    override suspend fun removeFromCart(product: CartItem) {
//        cartDao.deleteCart(product.toEntity())
//    }


}