package com.vanard.feature.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.vanard.common.UIState
import com.vanard.domain.model.Categories
import com.vanard.domain.model.Product
import com.vanard.domain.model.ProductList
import com.vanard.domain.usecase.ProductUseCase
import com.vanard.ui.base.BaseViewModel
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: ProductUseCase) : BaseViewModel() {

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

//    val products: StateFlow<ProductList> = combine(searchText, _products) { text, products ->
//        _isSearching.value = true
//
//        val result = if (text.isBlank()) {
//            products
//        } else {
//            delay(500L)
//            val filtered = products.products.filter {
//                it.doestMatchQuery(text)
//            }
//            ProductList(filtered)
//        }
//
//        _isSearching.value = false
//        result
//    }
//        .debounce(300L)
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _products.value)

    private val _selectedCategory: MutableState<Categories?> = mutableStateOf(Categories.AllItems)
    val selectedCategory
        get() = _selectedCategory

//    fun selectCategory(categories: Categories?) {
//        _selectedCategory.value = categories
//        _products.value = if (categories == null || categories == Categories.AllItems) {
//            _ogProducts.value.copy()
//        } else {
//            _ogProducts.value.copy(
//                products = _ogProducts.value.products.filter {
//                    it.doestMatchQuery(categories.value)
//                }
//            )
//        }
//    }

    fun selectCategory(category: Categories?) {
        _selectedCategory.value = category
        refreshFilteredList()
    }

    private fun refreshFilteredList() {
        val category = _selectedCategory.value

        _products.value = if (category == null || category == Categories.AllItems) {
            _ogProducts.value
        } else {
            _ogProducts.value.copy(
                products = _ogProducts.value.products.filter {
                    it.doestMatchQuery(category.value)
                }
            )
        }

//        val newList = if (category == null || category == Categories.AllItems) {
//            _ogProducts.value.products
//        } else {
//            _ogProducts.value.products.filter {
//                it.doestMatchQuery(category.value)
//            }
//        }
//
//        _products.value = ProductList(newList)
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    private fun setProducts(products: ProductList) {
        _ogProducts.value = products
        _products.value = products
    }

    fun getProducts() {
        if (isLoaded) return
        isLoaded = true

        viewModelScope.launch {
//            _uiState.value = UIState.Loading
            setLoading()
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
//                            _uiState.value = UIState.Success(products)
                            setSuccess()
                        }

                        else -> {
                            Log.d(TAG, "getProducts: error")
//                            _uiState.value = UIState.Error("BruH")
                            setError("Something went wrong")
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

//    fun pressFavorite(product: Product) {
//        val newListValue = _products.value.copy(products = _products.value.products.map { item ->
//            if (item.id == product.id) product else item
//        })
//
//        setProducts(newListValue)
//        updateProductItem(product)
//    }

    fun updateProductItem(product: Product) {
        safeScope.launch {
//            Log.d(TAG, "pressFavorite before: $product")
//            Log.d(TAG, "pressFavorite list before: \n${_products.value.products.take(3)}")
            setLoading()
            val updatedOgList = _ogProducts.value.products.map {
                if (it.id == product.id) it.copy(isFavorite = !it.isFavorite)
                else it
            }

            _ogProducts.value = ProductList(updatedOgList)
//            selectCategory(_selectedCategory.value)
            refreshFilteredList()

            product.apply {
                isFavorite = !isFavorite
            }
            Log.d(TAG, "pressFavorite after: $product")

            useCase.updateProduct(product)
            delay(50)
            setSuccess()
            Log.d(TAG, "pressFavorite list after: \n${_products.value.products.take(3)}")
        }
    }

    enum class Sort {
        ASC, DESC
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}