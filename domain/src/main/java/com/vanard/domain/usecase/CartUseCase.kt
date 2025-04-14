package com.vanard.domain.usecase

import com.vanard.core.common.UIState
import com.vanard.domain.model.Cart
import com.vanard.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class CartUseCase(private val cartRepository: CartRepository) {

    suspend fun getAllCarts(): Flow<UIState<List<Cart>>> {
        return cartRepository.getAllCarts()
    }

}