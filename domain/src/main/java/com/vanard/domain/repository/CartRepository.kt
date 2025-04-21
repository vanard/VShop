package com.vanard.domain.repository

import com.vanard.common.UIState
import com.vanard.domain.model.Cart
import com.vanard.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getAllCarts(): Flow<UIState<List<Cart>>>
//    suspend fun getAllLocalCarts(): Flow<UIState<List<Product>>>
//    suspend fun checkCartItemExist(id: Long): Boolean
//    suspend fun increaseQuantityCart(id: Long)
//    suspend fun decreaseQuantityCart(id: Long)

//    suspend fun getAllLocalCarts(): Flow<UIState<List<CartItem>>>
//    suspend fun getCartItemByUUID(uuid: String): Flow<UIState<List<CartItem>>>
//    suspend fun addToCart(product: CartItem)
//    suspend fun updateCartItem(product: CartItem)
//    suspend fun removeFromCart(product: CartItem)
}