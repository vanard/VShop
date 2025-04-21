package com.vanard.feature.detail

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vanard.common.UIState
import com.vanard.domain.model.Product
import com.vanard.domain.usecase.ProductUseCase
import com.vanard.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
//    private val cartUseCase: CartUseCase
) : BaseViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product get() = _product.asStateFlow()

    fun getProductById(id: Long) {
        safeScope.launch {
            productUseCase.getProductById(id).collect { state ->
                when (state) {
                    is UIState.Success -> {
                        if (state.data == null) {
                            _product.value = null
                            setError("Data is missing")
                        } else {
                            _product.value = state.data
                            setSuccess()
                        }
                        Log.d(TAG, "getProductById: $product")
                    }

                    else -> {
                        Log.d(TAG, "getProductById: error")
                    }
                }
            }

        }
    }

    fun loveProduct(product: Product) {
        viewModelScope.launch {
//            setLoading()
            val updateProduct = product.copy(isFavorite = !product.isFavorite)
            _product.value = updateProduct
            productUseCase.updateProduct(updateProduct)
//            delay(35)
//            setSuccess()
            Log.d(TAG, "loveProduct: \n${_product.value}")
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            productUseCase.increaseQuantityCart(product.id)
        }
    }

//    private fun Product.convertToCartItem(): CartItem =
//        CartItem(
//            this.id,
//            System.currentTimeMillis().toIso8601Utc(),
//            this.title,
//            this.price,
//            this.category,
//            this.image,
//            quantity = 1,
//            this.rating!!, //something fishy
//            this.description,
//            this.isFavorite
//        )

    companion object {
        private const val TAG = "DetailViewModel"
    }

}