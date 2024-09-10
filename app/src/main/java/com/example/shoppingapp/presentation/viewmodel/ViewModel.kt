package com.example.shoppingapp.presentation.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.common.ALL_USER_COLLECTION
import com.example.shoppingapp.common.ResultState
import com.example.shoppingapp.common.UserData
import com.example.shoppingapp.domain.useCase.CreateUserUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(private val useCase: CreateUserUseCase , private  val firebaseAuth: FirebaseAuth ) : ViewModel() {
    private val _signUpScreenState = MutableStateFlow(ResponseState())
    val signUpScreenState = _signUpScreenState.asStateFlow()

    private val _loginScreenState = MutableStateFlow(ResponseState())
    val loginScreenState = _loginScreenState.asStateFlow()


    private val _getUserByID = MutableStateFlow(GetUserById())
    val getUserByID = _signUpScreenState.asStateFlow()

    private val _userAddResult = MutableStateFlow(ResponseState())
    val userAddResult = _userAddResult.asStateFlow()


    fun addUserDataFromDB( userData: UserData) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.addUserDataFromDB(
                userId =firebaseAuth.uid.toString(),
                userData = userData,
                DbLocation = ALL_USER_COLLECTION
            ).collect{
                when(it){
                    is ResultState.Error -> {
                        _userAddResult.value = ResponseState(error = it.error)
                    }
                    ResultState.Loading -> {
                        _userAddResult.value = ResponseState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _userAddResult.value = ResponseState(userData = it.data)
                    }
                }
            }
        }
    }


    fun createUser(userData: UserData?) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.createUser(userData!!).collect {
                when (it) {
                    is ResultState.Error -> {
                        _signUpScreenState.value = ResponseState(error = it.error)
                    }

                    ResultState.Loading -> {
                        _signUpScreenState.value = ResponseState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _signUpScreenState.value = ResponseState(userData = it.data)


                    }
                }
                Log.d("TAG", "viewModel -> $it")
            }
        }
    }

    fun saveUserData(userData: UserData){


    }

    fun loginUser(userData: UserData?) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("EmailAndPass", "ViewModel")

            useCase.loginUser(userData!!).collect {
                when (it) {
                    is ResultState.Error -> {
                        _loginScreenState.value = ResponseState(error = it.error)
                    }

                    ResultState.Loading -> {
                        _loginScreenState.value = ResponseState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _loginScreenState.value = ResponseState(userData = it.data)
                    }
                }
            }
        }
    }

    fun getUserByID(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getUserById(uid).collect {
                when (it) {
                    is ResultState.Error -> {
                        _getUserByID.value = GetUserById(error = it.error)
                    }

                    ResultState.Loading -> {
                        _getUserByID.value = GetUserById(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getUserByID.value = GetUserById(userData = it.data)
                    }
                }


            }
        }
    }

}


data class GetUserById(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userData: UserData? = null,
)

data class ResponseState(
    val isLoading: Boolean = false,
    val error: String? = "",
    var userData: String? = "",
)

