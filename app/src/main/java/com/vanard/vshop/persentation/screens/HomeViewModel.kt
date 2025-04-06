package com.vanard.vshop.persentation.screens

import android.util.Log
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanard.vshop.common.UIState
import com.vanard.vshop.common.fetchState
import com.vanard.vshop.data.remote.ProductList
import com.vanard.vshop.domain.HomeUseCase
import com.vanard.vshop.domain.model.Categories
import com.vanard.vshop.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val usecase: HomeUseCase) : ViewModel() {
    private val _uiState: MutableStateFlow<UIState<ProductList>> = MutableStateFlow(UIState.Loading)
    val uiState: StateFlow<UIState<ProductList>>
        get() = _uiState

    private val searchTextFieldState = TextFieldState()

    private val _selectedCategory: MutableState<Categories?> = mutableStateOf(Categories.AllItems)
    val selectedCategory
        get() = _selectedCategory

    fun selectCategory(categories: Categories?) {
        _selectedCategory.value = categories
    }

    val searchState = snapshotFlow { searchTextFieldState.text }
        .debounce(500)
        .mapLatest { if (it.isBlank()) "Search clothes.." else it.toString() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ""
        )

    fun getProducts() {
        viewModelScope.launch {
            _uiState.value = UIState.Loading
            val result = withContext(Dispatchers.IO) {
                usecase.getAllProducts()
            }
            Log.d(TAG, "getProducts: $result")
            if (result.isNotEmpty()) {
                _uiState.value = UIState.Success(ProductList(result))
            }
//            when (result) {
//                is UIState.Success -> {
//                    _uiState.value = UIState.Success(result.data)
//                }
//                else -> _uiState.value = UIState.Error("BRUH")
//            }

//                usecase.getAllProducts()
//                    .collect { products ->
//                    Log.d(TAG, "getProducts: ${products.body()}")
//                    if (products.body() != null)
//                        _uiState.value = UIState.Success(products.body()!!)
//                    else
//                        _uiState.value = UIState.Error("BRUH")
//                }
        }
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}