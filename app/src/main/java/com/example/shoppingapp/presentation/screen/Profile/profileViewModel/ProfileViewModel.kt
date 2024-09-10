package com.example.shoppingapp.presentation.screen.Profile.profileViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.common.ResultState
import com.example.shoppingapp.common.UserData
import com.example.shoppingapp.domain.reop.Repo
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repo: Repo, private val auth: FirebaseAuth) :
    ViewModel() {
    private val _userDataVal = MutableStateFlow(UserDataRes())
    val userDataVal = _userDataVal.asStateFlow()

    init {
     getUserData()
    }

    fun getUserData() {
        viewModelScope.launch {
            repo.getUserById(auth.currentUser!!.uid).collect {
                when (it) {
                    is ResultState.Error -> {}
                    ResultState.Loading -> {
                        _userDataVal.value = UserDataRes(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _userDataVal.value = UserDataRes(isLoading = true)
                        _userDataVal.value = UserDataRes(data = it.data)
                    }
                }
            }
        }
    }

}

data class UserDataRes(
    val isLoading: Boolean = false,
    val data: UserData = UserData(),
)