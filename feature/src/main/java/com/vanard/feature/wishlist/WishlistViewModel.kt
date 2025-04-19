package com.vanard.feature.wishlist

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.vanard.common.UIState
import com.vanard.domain.model.Product
import com.vanard.domain.model.ProductList
import com.vanard.domain.usecase.WishlistUseCase
import com.vanard.feature.home.HomeViewModel.Companion.TAG
import com.vanard.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(private val useCase: WishlistUseCase) :
    BaseViewModel() {

//    private val _searchText = MutableStateFlow("")
//    val searchText = _searchText.asStateFlow()

//    private val _isSearching = MutableStateFlow(false)
//    val isSearching = _isSearching.asStateFlow()

    private val _products = MutableStateFlow(ProductList(listOf()))
    val products get() = _products.asStateFlow()

//    @OptIn(FlowPreview::class)
//    val products = searchText
//        .debounce(300L)
//        .onEach { _isSearching.update { true } }
//        .combine(_products) { text, products ->
//            if (text.isBlank()) {
//                products
//            } else {
//                delay(500L)
//                val filterItem = products.products.filter {
//                    it.doestMatchQuery(text)
//                }
//                ProductList(filterItem)
//            }
//        }
//        .onEach { _isSearching.update { false } }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            _products.value
//        )

//    fun onSearchTextChange(text: String) {
//        _searchText.value = text
//    }

    fun getAllProducts() {
        safeScope.launch {
            useCase.getAllProducts().collect { state ->
                when (state) {
                    is UIState.Success -> {
                        val products = ProductList(state.data)
                        Log.d(TAG, "getProducts: $products")
                        setSuccess()
                        _products.value = products
                    }

                    else -> {
                        Log.d(TAG, "getProducts: error")
                        setError("Something went wrong")
                    }
                }

            }
        }
    }

}