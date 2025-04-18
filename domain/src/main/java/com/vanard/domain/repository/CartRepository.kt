package com.vanard.domain.repository

import com.vanard.common.UIState
import com.vanard.domain.model.Cart
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getAllCarts(): Flow<UIState<List<Cart>>>
}