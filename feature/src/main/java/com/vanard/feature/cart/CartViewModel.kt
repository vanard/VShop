package com.vanard.feature.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanard.core.common.UIState
import com.vanard.domain.model.Cart
import com.vanard.domain.model.CartList
import com.vanard.domain.model.ProductList
import com.vanard.domain.usecase.CartUseCase
import com.vanard.feature.home.HomeViewModel.Companion.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val useCase: CartUseCase) : ViewModel() {

    private val _carts = MutableStateFlow(CartList(listOf()))

    fun getCarts() {
        viewModelScope.launch {
//            _uiState.value = UIState.Loading
            val result = withContext(Dispatchers.IO) {
                useCase.getAllCarts()
            }
            Log.d(TAG, "getCarts: $result")
            useCase.getAllCarts()
                .collect { state ->
                    when (state) {
                        is UIState.Success -> {
                            val cartList = CartList(state.data)
                            Log.d(TAG, "getCarts: $cartList")
                            _carts.value = cartList

//                            _uiState.value = UIState.Success(products)
                        }

                        else -> {
                            Log.d(TAG, "getCarts: error")
//                            _uiState.value = UIState.Error("BruH")
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