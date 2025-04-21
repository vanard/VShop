package com.vanard.domain.usecase

import com.vanard.common.UIState
import com.vanard.domain.model.Cart
import com.vanard.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class CartUseCase(private val cartRepository: CartRepository) {

    suspend fun getAllCarts(): Flow<UIState<List<Cart>>> {
        return cartRepository.getAllCarts()
    }

//    suspend fun getAllLocalCarts(): Flow<UIState<List<CartItem>>> {
//        return cartRepository.getAllLocalCarts()
//    }
//
//    suspend fun getChartItemByUUID(uuid: String): Flow<UIState<CartItem?>> {
//        return cartRepository.get(id)
//    }
//
//    suspend fun addToCart(product: CartItem) {
//        cartRepository.addToCart(product)
//    }
//
//    suspend fun updateCartItem(product: CartItem) {
//        cartRepository.updateCartItem(product)
//    }
//
//    suspend fun deleteCartItem(product: CartItem) {
//        cartRepository.removeFromCart(product)
//    }

}