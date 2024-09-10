package com.example.shoppingapp.presentation.screen.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.common.CategoryModel
import com.example.shoppingapp.common.ProductModel
import com.example.shoppingapp.common.ResultState
import com.example.shoppingapp.domain.useCase.CreateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class homeViewModel @Inject constructor(private val useCase: CreateUserUseCase) :ViewModel() {
    private var _allCategory = MutableStateFlow(AllCategoryState())
    var allCategory = _allCategory.asStateFlow()
    private var _allProduct = MutableStateFlow(AllProductState())
    var allProduct = _allProduct.asStateFlow()


    init {
        getAllCategory1()
        getAllProduct()

    }


    fun getAllCategory1(){
        viewModelScope.launch(Dispatchers.Default) {
            useCase.getCategory().collect{
                when(it){
                    is ResultState.Error -> {
                        _allCategory.value = AllCategoryState(isLoading = false)
                        _allCategory.value = AllCategoryState(error = it.error)
                    }
                    ResultState.Loading -> {
                        _allCategory.value = AllCategoryState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _allCategory.value = AllCategoryState(isLoading = false)
                        _allCategory.value = AllCategoryState(data =it.data)

                    }
                }
            }
        }
    }

    fun getAllProduct(){
        viewModelScope.launch (Dispatchers.IO){
            useCase.getProduct().collect{
                when(it){
                    is ResultState.Error -> {
                        _allProduct.value = AllProductState(isLoading = false)
                        _allProduct.value = AllProductState(error = it.error)
                    }
                    ResultState.Loading -> {
                        _allProduct.value = AllProductState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _allProduct.value = AllProductState(isLoading = false)
                        _allProduct.value = AllProductState(product = it.data)
                    }
                }
            }
        }
    }
}



data class AllCategoryState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: List<CategoryModel> = emptyList()
)

data class AllProductState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val product: List<ProductModel> = emptyList()
)