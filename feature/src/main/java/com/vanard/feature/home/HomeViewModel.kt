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
class HomeViewModel @Inject constructor(private val useCase: HomeUseCase) : ViewModel() {
    private val _uiState: MutableStateFlow<UIState<ProductList>> = MutableStateFlow(
        UIState.Loading
    )
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
                useCase.getAllProducts()
            }
            Log.d(TAG, "getProducts: $result")
            useCase.getAllProducts()
                .collect { state ->
                    when (state) {
                        is UIState.Success -> {
                            val products = ProductList(state.data)
                            Log.d(TAG, "getProducts: $products")
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