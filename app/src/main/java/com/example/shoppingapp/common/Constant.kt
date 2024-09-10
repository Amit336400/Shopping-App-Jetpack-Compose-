package com.example.shoppingapp.common


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


const val ALL_USER_COLLECTION= "allUserCollection"

const val CATEGORY_IMAGE_LOCATION = "categoryImageLocation"
const val PRODUCT_IMAGE_LOCATION = "productImageLocation"
const val ALL_PRODUCT_LOCATION = "allProductLocation"

const val ADD_TO_CART= "addToCArt113"
const val ALL_ORDERS= "allOrders113"
const val WISHLIST = "wishList113"
const val DATA_IS_PRESENT = "wishListDataIsPresent"

const val USER_DATA = "userData"
const val USER_LOCATION = "location1234"

const val ERROR = "Error113920"
const val SUCCESSFUL = "Successful113920"





fun calculatedPercentage(price: Int, percentage: Int): Int {
    return (price - (price * (percentage / 100.0)).toInt())
}



@Composable
fun LoadingDialog(
    show: Boolean,
    onDismissRequest: () -> Unit = {}
) {
    if (show) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                color = Color.Transparent,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.0f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}