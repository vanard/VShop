package com.vanard.feature.cart

import android.util.Log
import com.vanard.common.UIState
import com.vanard.domain.model.Product
import com.vanard.domain.usecase.product.ProductUseCase
import com.vanard.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val useCase: ProductUseCase,
//    private val cartUseCase: CartUseCase,
) : BaseViewModel() {

//    private val _carts = MutableStateFlow(CartList(listOf()))
//    val carts get() = _carts.asStateFlow()

    private val _total = MutableStateFlow(0.0)
    val total get() = _total.asStateFlow()

    private val _localCarts = MutableStateFlow(listOf<Product>())
    val localCarts get() = _localCarts.asStateFlow()

//    fun getCarts() {
//        if (isLoaded) return
//        isLoaded = true
//
//        safeScope.launch {
//            setLoading()
//            cartUseCase.getAllCarts()
//                .collect { state ->
//                    when (state) {
//                        is UIState.Success -> {
//                            val cartList = CartList(state.data)
//                            Log.d(TAG, "getCarts: $cartList")
//                            _carts.value = cartList
//                            setSuccess()
//                        }
//
//                        else -> {
//                            Log.d(TAG, "getCarts: error")
//                        }
//                    }
//                }
//        }
//    }

    fun getLocalCarts() {
        safeScope.launch {
            setLoading()
            useCase.getAllLocalCarts()
                .collect { state ->
                    when (state) {
                        is UIState.Success -> {
                            val localCart = state.data
                            Log.d(TAG, "getLocalCarts: $localCart")
                            _localCarts.value = localCart
                            calculateTotal()
                            setSuccess()
                        }

                        else -> {
                            setError("Error")
                            Log.d(TAG, "getCarts: error")
                        }
                    }
                }
        }
    }

    fun updateCartItem(cart: Product) {
        safeScope.launch {
            useCase.updateProduct(cart)
            Log.d(TAG, "updateCartItem: ${_localCarts.value[_localCarts.value.indexOf(cart)]}")
        }
    }

    fun removeCartItem(cart: Product) {
        safeScope.launch {
            useCase.updateProduct(cart.apply { quantityInCart = 0 })
            Log.d(TAG, "updateCartItem: ${_localCarts.value[_localCarts.value.indexOf(cart)]}")
        }
    }

    fun calculateTotal() {
        _total.value = _localCarts.value.sumOf { cart ->
            cart.price * cart.quantityInCart
        }
    }

    companion object {
        private const val TAG = "CartViewModel"
    }

}