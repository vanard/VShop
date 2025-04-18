package com.vanard.feature.cart

import android.util.Log
import com.vanard.common.UIState
import com.vanard.domain.model.CartItem
import com.vanard.domain.model.CartList
import com.vanard.domain.usecase.CartUseCase
import com.vanard.feature.home.HomeViewModel.Companion.TAG
import com.vanard.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val useCase: CartUseCase) : BaseViewModel() {

    private val _carts = MutableStateFlow(CartList(listOf()))
    val carts get() = _carts

    private val _localCarts = MutableStateFlow(listOf<CartItem>())
    val localCarts get() = _localCarts

    fun getCarts() {
        if (isLoaded) return
        isLoaded = true

        safeScope.launch {
            setLoading()
            useCase.getAllCarts()
                .collect { state ->
                    when (state) {
                        is UIState.Success -> {
                            val cartList = CartList(state.data)
                            Log.d(TAG, "getCarts: $cartList")
                            _carts.value = cartList
                            setSuccess()
                        }

                        else -> {
                            Log.d(TAG, "getCarts: error")
                        }
                    }
                }
        }
    }

    fun getLocalCarts() {
        safeScope.launch {
            useCase.getAllLocalCarts()
                .collect { state ->
                    when (state) {
                        is UIState.Success -> {
                            val localCart = state.data
                            Log.d(TAG, "getLocalCarts: $localCart")
                            _localCarts.value = localCart
                        }

                        else -> {
                            Log.d(TAG, "getCarts: error")
                        }
                    }
                }
        }
    }

    fun calculateTotal() {
        _carts.value.carts.forEach { cart ->
            cart.products.first()
        }
    }

}