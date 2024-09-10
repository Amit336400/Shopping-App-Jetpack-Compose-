package com.example.shoppingapp.presentation.screen.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.common.ResultState
import com.example.shoppingapp.common.UserLocation
import com.example.shoppingapp.domain.reop.Repo
import com.example.shoppingapp.presentation.viewmodel.ResponseState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class orderViewModel @Inject constructor(private val repo: Repo, private val auth: FirebaseAuth) :
    ViewModel() {
    val userId = auth.currentUser!!.uid.toString()
    private val _useLocation = MutableStateFlow(UserLocationResponce())
    val userLocation = _useLocation.asStateFlow()

    private val _addLocation = MutableStateFlow(ResponseState())
    val addLocation = _addLocation.asStateFlow()

    init {
        getLocation()
    }

    fun getLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getLocation(userId).collect {

                when (it) {
                    is ResultState.Error -> {
                        _useLocation.value = UserLocationResponce(isLoading = false)
                        _useLocation.value = UserLocationResponce(error = it.error)
                    }

                    ResultState.Loading -> {
                        _useLocation.value = UserLocationResponce(isLoading = true)

                    }

                    is ResultState.Success -> {
                        _useLocation.value = UserLocationResponce(isLoading = false)
                        _useLocation.value = UserLocationResponce(data = it.data)

                    }
                }

            }
        }
    }


    fun addLocation(userLocation: UserLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addUserLocation(userId = userId, userLocation = userLocation).collect {
                when (it) {
                    is ResultState.Error -> {
                        _addLocation.value = ResponseState(isLoading = false)
                        _addLocation.value = ResponseState(error = it.error)
                    }

                    ResultState.Loading -> {
                        _addLocation.value = ResponseState(isLoading = true)

                    }

                    is ResultState.Success -> {
                        _addLocation.value = ResponseState(isLoading = false)
                        _addLocation.value = ResponseState(userData = it.data)

                    }
                }

            }
        }
    }



}

data class UserLocationResponce(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: UserLocation = UserLocation(),
)