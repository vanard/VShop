package com.vanard.domain.repository

import com.vanard.common.UIState
import com.vanard.domain.model.Cart
import com.vanard.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getAllCarts(): Flow<UIState<List<Cart>>>
    suspend fun getAllLocalCarts(): Flow<UIState<List<CartItem>>>
}