package com.example.shoppingapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.common.ProductModel
import com.example.shoppingapp.common.ResultState
import com.example.shoppingapp.domain.useCase.CreateUserUseCase
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class testViewModel @Inject constructor(val useCase: CreateUserUseCase) : ViewModel() {

    private val _data = MutableStateFlow<List<ProductModel>>(emptyList())
    val data1: StateFlow<List<ProductModel>> = _data

    private var lastVisible: DocumentSnapshot? = null
     var isLoading = false


    private fun loadData() {
        if (isLoading) return

        isLoading = true
        viewModelScope.launch {
            useCase.getProductDAta(lastVisible, 10).collect {
                when (it) {
                    is ResultState.Error -> {

                    }

                    ResultState.Loading -> {
                        isLoading = true
                    }

                    is ResultState.Success -> {
                        lastVisible = it.data.last() as DocumentSnapshot?
                        _data.value = _data.value + it.data
                    }
                }
            }

        }
    }

    fun loadMoreData() {
        if (lastVisible != null) {
            loadData()
        }
    }

}
