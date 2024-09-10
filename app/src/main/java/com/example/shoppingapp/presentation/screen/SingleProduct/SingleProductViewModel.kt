package com.example.shoppingapp.presentation.screen.SingleProduct

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.common.DATA_IS_PRESENT
import com.example.shoppingapp.common.ProductModel
import com.example.shoppingapp.common.ResultState
import com.example.shoppingapp.common.setProduct
import com.example.shoppingapp.domain.reop.Repo
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleProductViewModel @Inject constructor(
    private val repo: Repo,
    private val Auth: FirebaseAuth,
) : ViewModel() {
    val userId = Auth.currentUser?.uid.toString()

    private val _productItem = MutableStateFlow(productResponce())
    var productItem = _productItem

    private val _wishList = MutableStateFlow(StringResponce())
    val wishList = _wishList

    private val _checkWishList = MutableStateFlow(StringResponce())
    private val checkWishList = _checkWishList


    private val _addToCartVal = MutableStateFlow(StringResponce())
    val addTocartVal = _addToCartVal.asStateFlow()



    var checkItemInWishList: Boolean = false


    fun dataLoading(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getSingleProduct(productId).collect {
                when (it) {
                    is ResultState.Error -> {
                        _productItem.value = productResponce(isLoading = false)
                        _productItem.value = productResponce(error = it.error)
                    }

                    ResultState.Loading -> {
                        _productItem.value = productResponce(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _productItem.value = productResponce(isLoading = false)
                        _productItem.value = productResponce(data = it.data)
                    }
                }
            }
        }

    }

    fun addWishList(itemId: String) {
        viewModelScope.launch {
            repo.addWishList(
                productId = itemId,
                userId = userId
            ).collect {
                when (it) {
                    is ResultState.Error -> {}
                    ResultState.Loading -> {}
                    is ResultState.Success -> {
                        _wishList.value = StringResponce(data = it.data)
                    }
                }
            }
        }
    }

    fun checkItemInWishList(itemId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.checkWishList(userId = userId, productId = itemId).collect {
                when (it) {
                    is ResultState.Error -> {}
                    ResultState.Loading -> {}
                    is ResultState.Success -> {
                        _wishList.value = StringResponce(data = it.data)
                    }
                }
            }
        }
        if (checkWishList.value.data == DATA_IS_PRESENT) {
            checkItemInWishList = true
        }

    }


    fun addToCart(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addToCart(productId = productId, userId = userId).collect {
                when(it){
                    is ResultState.Error -> {
                        _addToCartVal.value = StringResponce(isLoading = false)
                    }
                    ResultState.Loading -> {_addToCartVal.value =StringResponce(isLoading = true) }
                    is ResultState.Success -> {
                        _addToCartVal.value = StringResponce(data = it.data)
                    }
                }

            }
        }

    }


}


data class productResponce(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: ProductModel = ProductModel(),
)

data class StringResponce(
    val isLoading: Boolean = false,
    val error: String = "",
    var data: String = "",
)

data class AddTOCart(
    val isLoading: Boolean = true,
    val error: String = "",
    val data: List<setProduct> = emptyList(),
)