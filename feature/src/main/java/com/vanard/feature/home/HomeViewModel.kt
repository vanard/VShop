package com.vanard.feature.home

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanard.core.common.UIState
import com.vanard.domain.model.Categories
import com.vanard.domain.model.ProductList
import com.vanard.domain.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val useCase: HomeUseCase) : ViewModel() {

//    sealed interface SearchState {
//        object Empty : SearchState
//        data class UserQuery(val query: String) : SearchState
//    }

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

    @OptIn(FlowPreview::class)
    val products = searchText
        .debounce(300L)
        .onEach { _isSearching.update { true } }
        .combine(_products) { text, products ->
            if(text.isBlank()) {
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

//    private val searchTextFieldState = TextFieldState()

    private val _selectedCategory: MutableState<Categories?> = mutableStateOf(Categories.AllItems)
    val selectedCategory
        get() = _selectedCategory

    fun selectCategory(categories: Categories?) {
        _selectedCategory.value = categories
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    private fun setProducts(products: ProductList){
        _products.value = products
    }

//    val searchState = snapshotFlow { searchTextFieldState.text }
//        .debounce(500)
//        .mapLatest { if (it.isBlank()) "Search clothes.." else it.toString() }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(),
//            initialValue = ""
//        )

//    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
//    private val searchTextState: StateFlow<SearchState> = snapshotFlow { searchTextFieldState.text }
//        .debounce(500)
//        .mapLatest { if (it.isBlank()) SearchState.Empty else SearchState.UserQuery(it.toString()) }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2000),
//            initialValue = SearchState.Empty
//        )

//    fun observeUserSearch() = viewModelScope.launch {
//        searchTextState.collectLatest { searchState ->
//            when (searchState) {
//                is SearchState.Empty -> _uiState.update { UIState.Success(ProductList(listOf())) }
//                is SearchState.UserQuery -> searchAllCharacters(searchState.query)
//            }
//        }
//    }

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

    companion object {
        const val TAG = "HomeViewModel"
    }
}