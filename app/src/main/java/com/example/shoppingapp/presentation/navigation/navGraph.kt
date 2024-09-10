package com.example.shoppingapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.shoppingapp.presentation.screen.App
import com.example.shoppingapp.presentation.screen.cart.CartScreen
import com.example.shoppingapp.presentation.screen.Home.Home
import com.example.shoppingapp.presentation.screen.Profile.EditProfile
import com.example.shoppingapp.presentation.screen.loginSignUp.LoginScreen
import com.example.shoppingapp.presentation.screen.Profile.ProfileHome
import com.example.shoppingapp.presentation.screen.loginSignUp.SignUpScreen
import com.example.shoppingapp.presentation.screen.SingleProduct.SingleProductScreen
import com.example.shoppingapp.presentation.screen.order.OrderScreen
import com.example.shoppingapp.presentation.screen.search.SearchProductScreen
import com.example.shoppingapp.presentation.screen.search.searchViewModel
import com.example.shoppingapp.presentation.screen.wishList.WishListScreen

@Composable
fun navGraph(navController: NavHostController, stsrtingDest: SubNavigation) {

    NavHost(navController = navController, startDestination = stsrtingDest) {

        navigation<SubNavigation.LoginSignUpScreen>(startDestination = Routs.LoginScreenRout) {
            composable<Routs.LoginScreenRout> {
                LoginScreen(navController = navController)
            }
            composable<Routs.SignUpScreenRout> {
                SignUpScreen(navController = navController)
            }
        }

        navigation<SubNavigation.MainHomeScreen>(startDestination = Routs.HomeScreenRout) {

            composable<Routs.HomeScreenRout> {
               Home(navigation = navController)
            }

            composable<Routs.ProfileScreenRout> {
                ProfileHome(navController = navController)
            }
            composable<Routs.CartScreenRout> {
              CartScreen(navController = navController)
            }
            composable<Routs.WishListScreenRout> {
                WishListScreen()
            }
        }

        composable<Routs.EditProfile> {
            EditProfile()
        }
        composable<Routs.SearchRouts> {
            SearchProductScreen()
        }

        composable<Routs.SingleProductScreen> {
            val productId : Routs.SingleProductScreen = it.toRoute()
            SingleProductScreen(productId =productId.productId.toString(), navController = navController)
        }

        composable<Routs.OrederScreenRout> {
            val productId : Routs.OrederScreenRout = it.toRoute()
            OrderScreen(productId =productId.productId.toString(), navController = navController, productQuen =productId.productQwn )
        }


        composable<Routs.AppRout> {
            App(navController = navController)
        }

    }
}