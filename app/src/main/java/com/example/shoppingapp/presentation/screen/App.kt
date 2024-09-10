package com.example.shoppingapp.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shoppingapp.domain.di.DiModel
import com.example.shoppingapp.presentation.navigation.ButtomNavigationView
import com.example.shoppingapp.presentation.navigation.Routs
import com.example.shoppingapp.presentation.navigation.SubNavigation
import com.example.shoppingapp.presentation.navigation.navGraph
import com.example.shoppingapp.ui.theme.lightPink11
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(
    navController: NavHostController,
    firebaseAuth: FirebaseAuth = DiModel.provideFirebaseAuth(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val shouldShowBottomBar = remember { mutableStateOf(false) }
    val shouldShowTopBar = remember { mutableStateOf(false) }
    LaunchedEffect(currentDestination) {
        shouldShowBottomBar.value = when (currentDestination) {
            Routs.HomeScreenRout::class.qualifiedName, Routs.ProfileScreenRout::class.qualifiedName, Routs.CartScreenRout::class.qualifiedName -> true
            else -> false
        }
        shouldShowTopBar.value = when (currentDestination) {
            Routs.SignUpScreenRout::class.qualifiedName, Routs.LoginScreenRout::class.qualifiedName, Routs.CartScreenRout::class.qualifiedName -> false
            else -> true
        }

    }
    val startScreen = if (firebaseAuth.currentUser == null) {
        SubNavigation.LoginSignUpScreen
    } else {
        SubNavigation.MainHomeScreen
    }

    Scaffold(
        topBar = {
            if (shouldShowTopBar.value) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = lightPink11
                    ),
                    title = { Text(text = "Shopping App") },
                    actions = {
                        IconButton(onClick = { navController.navigate(Routs.SearchRouts) }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = ""
                            )
                        }
                    },

                    )
            }
        },
        bottomBar = {
            if (shouldShowBottomBar.value) {
                ButtomNavigationView(navigation = navController)
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = if (shouldShowBottomBar.value) {
                    it.calculateBottomPadding()
                } else 0.dp
            )
        ) {
            navGraph(navController = navController, stsrtingDest = startScreen)
        }
    }
}



