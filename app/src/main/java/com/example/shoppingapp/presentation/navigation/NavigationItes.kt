package com.example.shoppingapp.presentation.navigation

import com.example.shoppingapp.common.ProductModel
import kotlinx.serialization.Serializable

sealed class SubNavigation {
    @Serializable
    object LoginSignUpScreen : SubNavigation()

    @Serializable
    object MainHomeScreen : SubNavigation()
}

sealed class Routs {
    @Serializable
    object AppRout

    @Serializable
    object LoginScreenRout

    @Serializable
    object SignUpScreenRout

    @Serializable
    object HomeScreenRout

    @Serializable
    object ProfileScreenRout

    @Serializable
    object WishListScreenRout

    @Serializable
    object CartScreenRout

    @Serializable
    object EditProfile

    @Serializable
    object SearchRouts

    @Serializable
    data class OrederScreenRout(val productId : String,val productQwn : String)

    @Serializable
    data class SingleProductScreen(val productId : String)
}