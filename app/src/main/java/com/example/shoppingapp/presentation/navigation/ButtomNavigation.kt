package com.example.shoppingapp.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shoppingapp.ui.theme.lightPink11


sealed class ButtonNavigationItems<T>(
    val label: String,
    val selectIcon: ImageVector,
    val unSelectIcon: ImageVector,
    val rout: T,
) {

    data object home : ButtonNavigationItems<Routs.HomeScreenRout>(
        label = "Home",
        selectIcon = Icons.Filled.Home,
        unSelectIcon = Icons.Outlined.Home,
        rout = Routs.HomeScreenRout
    )


    data object Wish : ButtonNavigationItems<Routs.CartScreenRout>(
        label = "wish",
        selectIcon = Icons.Filled.ShoppingCart,
        unSelectIcon = Icons.Outlined.ShoppingCart,
        rout = Routs.CartScreenRout
    )


    data object Profile : ButtonNavigationItems<Routs.ProfileScreenRout>(
        label = "Profile",
        selectIcon = Icons.Filled.Person,
        unSelectIcon = Icons.Outlined.Person,
        rout = Routs.ProfileScreenRout
    )
}


@Composable
fun ButtomNavigationView(navigation: NavController) {
    val list = remember {
        listOf(
            ButtonNavigationItems.home,
            ButtonNavigationItems.Wish,
            ButtonNavigationItems.Profile
        )
    }

        BottomView(list = list, navigation = navigation)




}
@Composable
private fun BottomView(list: List<ButtonNavigationItems<out Any>>, navigation: NavController) {
    var selectedTabIndex by rememberSaveable {
        mutableStateOf(0)
    }


    BottomNavigation (
        backgroundColor = lightPink11,
    ){
        val navBackStackEntry by navigation.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination

        list.forEachIndexed { index, buttonNavigationItems ->
            var isSelected = currentRoute?.hierarchy?.any {
                it.route == buttonNavigationItems.rout::class.qualifiedName
            } == true


            BottomNavigationItem(
                //modifier = Modifier.padding(0.dp),
                selected = isSelected,
                onClick = {
                    //selectedTabIndex = index
                    navigation.navigate(buttonNavigationItems.rout) {
                        popUpTo(navigation.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },

                icon = {

                    Icon(
                        modifier = Modifier.padding(0.dp),
                        imageVector = if (isSelected) {
                            buttonNavigationItems.selectIcon
                        }else{
                            buttonNavigationItems.unSelectIcon
                        }
                       , contentDescription = null)

                },

            )

        }

    }


}

