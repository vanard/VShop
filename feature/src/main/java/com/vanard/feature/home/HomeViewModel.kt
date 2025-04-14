package com.vanard.feature.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanard.core.common.UIState
import com.vanard.domain.model.Categories
import com.vanard.domain.model.ProductList
import com.vanard.domain.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: HomeUseCase) : ViewModel() {

    private val _uiState: MutableStateFlow<UIState<ProductList>> = MutableStateFlow(
        UIState.Loading
    )
    val uiState: StateFlow<UIState<ProductList>>
        get() = _uiState.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _products = MutableStateFlow(ProductList(listOf()))
    private val _ogProducts = MutableStateFlow(ProductList(listOf()))

    private val _sort = MutableStateFlow(Sort.ASC)

    @OptIn(FlowPreview::class)
    val products = searchText
        .debounce(300L)
        .onEach { _isSearching.update { true } }
        .combine(_products) { text, products ->
            if (text.isBlank()) {
                products
            } else {
                delay(500L)
                val filterItem = products.products.filter {
                    it.doestMatchQuery(text)
                }
                ProductList(filterItem)
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _products.value
        )

    private val _selectedCategory: MutableState<Categories?> = mutableStateOf(Categories.AllItems)
    val selectedCategory
        get() = _selectedCategory

    fun selectCategory(categories: Categories?) {
        _selectedCategory.value = categories
        _products.value = if (categories == null || categories == Categories.AllItems) {
            _ogProducts.value.copy()
        } else {
            _ogProducts.value.copy(
                products = _ogProducts.value.products.filter {
                    it.doestMatchQuery(categories.value)
                }
            )
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    private fun setProducts(products: ProductList) {
        _ogProducts.value = products
        _products.value = products
    }

    fun getProducts() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val result = withContext(Dispatchers.IO) {
                useCase.getAllProducts()
            }
            Log.d(TAG, "getProducts: $result")
            useCase.getAllProducts()
                .collect { state ->
                    when (state) {
                        is UIState.Success -> {
                            val products = ProductList(state.data)
                            Log.d(TAG, "getProducts: $products")
                            setProducts(products)
                            _uiState.value = UIState.Success(products)
                        }

                        else -> {
                            Log.d(TAG, "getProducts: error")
                            _uiState.value = UIState.Error("BruH")
                        }
                    }
                }
        }
    }

    fun sortProducts() {
        _sort.value = if (_sort.value == Sort.ASC) Sort.DESC else Sort.ASC
        val sorted = if (_sort.value == Sort.ASC)
            _products.value.products.sortedBy { it.title }
        else
            _products.value.products.sortedByDescending { it.title }

        _products.value = _products.value.copy(products = sorted)
    }

    enum class Sort {
        ASC, DESC
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}