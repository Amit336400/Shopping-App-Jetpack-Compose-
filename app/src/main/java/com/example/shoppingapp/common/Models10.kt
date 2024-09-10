package com.example.shoppingapp.common

data class UserData(
    val name:String="",
    val email :String="",
    val password:String="",
    val phone:String="",
)

data class ParentUserData(
    val uid : String ="",
    val userData: UserData = UserData()
)

