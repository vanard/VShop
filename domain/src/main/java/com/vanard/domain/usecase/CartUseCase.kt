package com.vanard.domain.usecase

import com.vanard.common.UIState
import com.vanard.domain.model.Cart
import com.vanard.domain.model.CartItem
import com.vanard.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class CartUseCase(private val cartRepository: CartRepository) {

    suspend fun getAllCarts(): Flow<UIState<List<Cart>>> {
        return cartRepository.getAllCarts()
    }

    suspend fun getAllLocalCarts(): Flow<UIState<List<CartItem>>> {
        return cartRepository.getAllLocalCarts()
    }

}