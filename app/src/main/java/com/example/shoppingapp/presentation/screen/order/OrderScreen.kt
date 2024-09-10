package com.example.shoppingapp.presentation.screen.order


import AddressPopup
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.shoppingapp.common.ERROR
import com.example.shoppingapp.common.LoadingDialog
import com.example.shoppingapp.common.calculatedPercentage
import com.example.shoppingapp.presentation.screen.SingleProduct.SingleProductViewModel
import com.example.shoppingapp.presentation.screen.cart.QuantitySelector
import com.example.shoppingapp.ui.theme.backGraund
import com.example.shoppingapp.ui.theme.backGraundCard
import com.example.shoppingapp.ui.theme.buttonColour


@Composable
fun OrderScreen(
    navController: NavController,
    productId: String,
    productQuen: String,
    viewModel: orderViewModel = hiltViewModel(),
    singleScreen: SingleProductViewModel = hiltViewModel(),
) {
    val locationData = viewModel.userLocation.collectAsStateWithLifecycle().value
    val product = singleScreen.productItem.collectAsStateWithLifecycle().value

    val quantity = remember { mutableStateOf<Int>(0) }
    val totalPrice = remember { mutableStateOf<Int>(0) }


    LoadingDialog(show = locationData.isLoading)
    LoadingDialog(show = product.isLoading)

    if (!locationData.data.address.isNullOrEmpty()) {
        LaunchedEffect(Unit) {
            singleScreen.dataLoading(productId)
        }
        val data = locationData.data
        val productData = product.data

        if (!product.data.productName.isNullOrEmpty()) {
            val productDisPrize = remember {
                mutableStateOf(
                    calculatedPercentage(
                        price = productData.productPrize,
                        percentage = productData.productDiscaountInPersebtage
                    )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backGraund)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(.94f)
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        ) {
                            Box(modifier = Modifier) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = backGraundCard
                                    )
                                ) {
                                    Text(
                                        text = "Address",
                                        fontSize = 25.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Name -  ${data.name} ",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Address - ${data.address}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Pin - ${data.pin}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "State - ${data.state}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Phone No - ${data.phoneNo}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                }
                                var showDialog by remember { mutableStateOf(false) }

                                IconButton(
                                    onClick = {
                                        showDialog = true
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                AddressPopup(
                                    showDialog = showDialog,
                                    onDismissRequest = { showDialog = false },
                                    name = data.name,
                                    address = data.address,
                                    pin = data.pin,
                                    state = data.state,
                                    phoneNo = data.phoneNo
                                )
                            }

                        }
                        Column(modifier = Modifier.fillMaxWidth()) {

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
                                                model = productData.productPhoto,
                                                contentDescription = null
                                            )
                                            QuantitySelector(
                                                currentQuantity = quantity.value,
                                                minQuantity = productQuen.toInt(),
                                                onQuantityChange = { newQuantity ->
                                                    quantity.value = newQuantity
                                                }
                                            )
                                        }

                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(
                                                text = productData.productName,
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(8.dp)
                                            )

                                            Text(
                                                text = "₹ ${productDisPrize.value}",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(3.dp)
                                            )
                                        }
                                    }

                                }
                            }

                        }
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row (modifier = Modifier.fillMaxWidth().fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween){
                            Text(fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                text = "Total Price - ${quantity.value*productDisPrize.value}")
                            Button(    shape = RoundedCornerShape(0.dp),
                                colors = ButtonDefaults.buttonColors(
                                containerColor = buttonColour
                            ),
                                onClick = {

                            }) {
                                Text(text = "Buy")
                            }

                        }

                    }

                }
            }
        }

    } else if (locationData.error == ERROR) {
        AddressPopup(
            showDialog = locationData.data.address.isNullOrEmpty(),
            onDismissRequest = { !locationData.data.address.isNullOrEmpty() },
        )
        viewModel.getLocation()
    }

}