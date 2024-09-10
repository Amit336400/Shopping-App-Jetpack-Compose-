package com.example.shoppingapp.presentation.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.common.ADD_TO_CART
import com.example.shoppingapp.common.ProductModel
import com.example.shoppingapp.common.ResultState
import com.example.shoppingapp.common.setProduct
import com.example.shoppingapp.domain.reop.Repo
import com.example.shoppingapp.presentation.screen.SingleProduct.StringResponce
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class cartViewModel @Inject constructor(private val repo: Repo, val auth: FirebaseAuth) :
    ViewModel() {
    val userId = auth.currentUser!!.uid.toString()

    private val _cartList = MutableStateFlow(CartState())
    var cartList = _cartList.asStateFlow()

    private val _cartProductList = MutableStateFlow(CartProductState())
    var cartProductList = _cartProductList.asStateFlow()

    private val _deleteProductList = MutableStateFlow(StringResponce())
    var deleteProductList = _deleteProductList.asStateFlow()

    init {
        getCartProductIdData()
    }

    fun getCartListOfProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.grtProductByIds(cartList.value.data).collect {
                when (it) {
                    is ResultState.Error -> {
                        _cartProductList.value = CartProductState(isLoading = false)
                    }

                    ResultState.Loading -> {
                        _cartProductList.value = CartProductState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _cartProductList.value = CartProductState(isLoading = false)
                        _cartProductList.value = CartProductState(data = it.data)

                    }
                }
            }
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteItem(location = ADD_TO_CART, productId = productId, userId = userId)
                .collect {
                    when (it) {
                        is ResultState.Error -> {
                            _deleteProductList.value = StringResponce(isLoading = false)
                            _deleteProductList.value = StringResponce(error = it.error)

                        }
                        ResultState.Loading -> {
                            _deleteProductList.value =
                                StringResponce(isLoading = true)
                        }

                        is ResultState.Success -> {
                            _deleteProductList.value = StringResponce(isLoading = false)
                            _deleteProductList.value = StringResponce(data = it.data)
                            _cartProductList.value = _cartProductList.value.copy(
                                data = _cartProductList.value.data.filter { product ->
                                    product.productId != productId
                                }
                            )

                        }
                    }
                }
        }
    }


    fun getCartProductIdData() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getToCartItems(userId = auth.currentUser!!.uid).collect {
                when (it) {
                    is ResultState.Error -> {
                        _cartList.value = CartState(isLoading = false)
                        _cartList.value = CartState(error = it.error)
                    }

                    ResultState.Loading -> {
                        _cartList.value = CartState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _cartList.value = CartState(isLoading = false)
                        _cartList.value = CartState(data = it.data)

                    }
                }
            }

        }

    }

}

data class CartState(
    val isLoading: Boolean = false,
    val data: List<setProduct> = emptyList(),
    val error: String = "",
)

data class CartProductState(
    var isLoading: Boolean = false,
    val data: List<ProductModel> = emptyList(),
    val error: String = "",
)