package com.example.shoppingapp.presentation.screen.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shoppingapp.common.calculatedPercentage
import com.example.shoppingapp.presentation.navigation.Routs
import com.example.shoppingapp.presentation.screen.SingleProduct.SingleProductViewModel
import com.example.shoppingapp.ui.theme.backGraund
import com.example.shoppingapp.ui.theme.backGraundCard
import com.example.shoppingapp.ui.theme.buttonColour
import com.example.shoppingapp.ui.theme.lightPink11

@Composable
fun CartScreen(
    viewModel: cartViewModel = hiltViewModel(),
    navController: NavController
) {
    Box(modifier = Modifier.fillMaxSize().background(backGraund)){

    var cartData = viewModel.cartList.collectAsStateWithLifecycle().value
    var cartProductData = viewModel.cartProductList.collectAsStateWithLifecycle().value

    if (cartData.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backGraund),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    else if (!cartData.data.isNullOrEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.getCartListOfProduct()
        }
            if (cartProductData.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backGraund),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator()

                }

            }
            else if (!cartProductData.error.isNullOrEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backGraund),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(text = "${cartData.error}")

                }

            }

            else if (!cartProductData.data.isNullOrEmpty()) {
                val data = cartProductData.data

                // Manage quantities with a Map
                val quantityMap = remember { mutableStateMapOf<String, Int>() }

                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight(0.93f)
                            .fillMaxWidth()
                            .background(backGraund)
                    ) {
                        items(data) { product ->
                            val quantity = quantityMap[product.productId] ?: 1 // Default quantity

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp),
                                colors = CardDefaults.cardColors(containerColor = backGraundCard),
                            ) {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    // Content of the card
                                    Row {
                                        Column(
                                            modifier = Modifier.padding(6.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            AsyncImage(
                                                modifier = Modifier
                                                    .background(Color.Black)
                                                    .height(100.dp)
                                                    .width(90.dp),
                                                model = product.productPhoto,
                                                contentDescription = null
                                            )
                                        }

                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(
                                                text = product.productName,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(10.dp)
                                            )
                                            val productDisPrize = remember {
                                                mutableStateOf(
                                                    calculatedPercentage(
                                                        price = product.productPrize,
                                                        percentage = product.productDiscaountInPersebtage
                                                    )
                                                )
                                            }
                                            Text(
                                                text = "₹ ${productDisPrize.value}",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(3.dp)
                                            )
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxSize(),
                                                verticalArrangement = Arrangement.Top
                                            ) {
                                                Button(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    onClick = {
                                                      navController.navigate(Routs.OrederScreenRout(
                                                          productId = product.productId.toString(),
                                                          productQwn = product.minOrderQuen
                                                      ))
                                                    },
                                                    colors = ButtonDefaults.buttonColors(containerColor = buttonColour),
                                                    shape = RoundedCornerShape(0.dp)
                                                ) {
                                                    Text(text = "Check Out", color = Color.White)
                                                }
                                            }
                                        }
                                    }

                                    // Delete button at the top-right corner
                                    IconButton(
                                        onClick = {
                                            // Handle delete action
                                            viewModel.deleteProduct(productId = product.productId.toString())
                                        },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }


                }
            }
    }
}
}

    @Composable
    fun QuantitySelector(
        currentQuantity: Int,
        onQuantityChange: (Int) -> Unit,
        minQuantity: Int,
        maxQuantity: Int = 100,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(backGraund)
        ) {
            IconButton(
                onClick = {
                    if (currentQuantity > minQuantity) {
                        onQuantityChange(currentQuantity - 1)
                    }
                },
                enabled = currentQuantity > minQuantity
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "Decrease quantity")
            }

            Text(
                text = currentQuantity.toString(),
                modifier = Modifier.padding(horizontal = 5.dp)
            )

            IconButton(
                onClick = {
                    if (currentQuantity < maxQuantity) {
                        onQuantityChange(currentQuantity + 1)
                    }
                },
                enabled = currentQuantity < maxQuantity
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase quantity")
            }
        }
    }
