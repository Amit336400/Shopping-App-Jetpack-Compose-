package com.example.shoppingapp.presentation.screen.SingleProduct

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shoppingapp.common.LoadingDialog
import com.example.shoppingapp.common.SUCCESSFUL
import com.example.shoppingapp.common.calculatedPercentage
import com.example.shoppingapp.presentation.navigation.Routs
import com.example.shoppingapp.ui.theme.backGraund
import com.example.shoppingapp.ui.theme.buttonColour
import com.example.shoppingapp.ui.theme.buttonColour2


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SingleProductScreen(
    productId: String,
    viewModel: SingleProductViewModel = hiltViewModel(),
    navController: NavController,
) {
    LaunchedEffect(key1 = true) {
        viewModel.dataLoading(productId)
        //viewModel.checkItemInWishList(productId.toString())
    }
    val res = viewModel.productItem.collectAsStateWithLifecycle().value
    val addToCartDAta = viewModel.addTocartVal.collectAsStateWithLifecycle().value

    if (addToCartDAta.data == SUCCESSFUL) {
        navController.navigate(Routs.CartScreenRout)
        addToCartDAta.data = ""
    }

            LoadingDialog(addToCartDAta.isLoading)
            LoadingDialog(show = res.isLoading)


        if (!(res.error.isNullOrEmpty())) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backGraund),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "${res.error}")
            }

        }
        else if (!(res.data.productName.isNullOrEmpty())) {
            val productSet = res.data
            val productDisPrize = remember {
                mutableStateOf(
                    calculatedPercentage(
                        price = productSet.productPrize,
                        percentage = productSet.productDiscaountInPersebtage
                    )
                )
            }
            val images = listOf(productSet.productPhoto)
            var isExpanded by remember { mutableStateOf(false) }
            val description = productSet.productDisp
            val shortDescription = description.take(100) // Shortened description

            Scaffold {
                it
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backGraund)
                ) {


                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        // Scrollable Content
                        Column(
                            modifier = Modifier
                                .fillMaxSize(.92f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            // Image Slider
//                        ImageSlider(images = images)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                            ) {
                                // Image
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Fit,
                                    model = productSet.productPhoto,
                                    contentDescription = null
                                )

                                /*
                                                        // Share button at the top end
                                                        IconButton(
                                                            onClick = {
                                                               viewModel.addWishList(productId.toString())
                                                            },
                                                            modifier = Modifier
                                                                .align(Alignment.TopEnd)
                                                                .padding(8.dp) // Optional padding
                                                        ) {
                                                            Icon(
                                                                imageVector = if (viewModel.checkItemInWishList) {
                                                                    Icons.Filled.Favorite
                                                                } else {
                                                                    Icons.Outlined.FavoriteBorder
                                                                } ,contentDescription = "Share"
                                                            )
                                                        }
                                              */
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            // Product Name
                            Text(
                                text = "${productSet.productName}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(8.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Divider
                            Divider(
                                color = Color.LightGray,
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // MRP Section
                            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "M.R.P: ",
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = "₹ ${productSet.productPrize}",
                                        fontSize = 18.sp,
                                        color = Color.Gray,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Off -${productSet.productDiscaountInPersebtage}%",
                                        fontSize = 18.sp,
                                        color = Color.Red,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "₹ ",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "${productDisPrize.value}",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Minimum Quantity
                            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(text = "Minimum Quantity: ")
                                Text(
                                    text = "${productSet.minOrderQuen}",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            //Display Size All
                            if (!productSet.productFreeSize) {

                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            // Product Description with "Read More"
                            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                                Text(
                                    text = "Description",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                // Toggle between short and full description
                                Text(
                                    text = if (isExpanded) description else shortDescription,
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    lineHeight = 22.sp
                                )

                                ClickableText(
                                    text = AnnotatedString(if (isExpanded) "Read Less" else "Read More"),
                                    onClick = { isExpanded = !isExpanded },
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }


                        // Bottom Buttons
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.addToCart(productId = productId)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = buttonColour
                                    ),
                                    shape = RoundedCornerShape(0.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                ) {
                                    Text(
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        text = "Add To Cart"
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        navController.navigate(Routs.OrederScreenRout(productId = productId.toString()
                                            , productQwn =productSet.minOrderQuen))

                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = buttonColour2
                                    ),
                                    shape = RoundedCornerShape(0.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                ) {
                                    Text(
                                        color = Color.White,
                                        text = "Buy Now"
                                    )
                                }
                            }
                        }


                    }
                }
            }
        }
    }



@Composable
fun ImageSlider(images: List<String>) {
    val pagerState = rememberPagerState(pageCount = { images.count() })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = images.size,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { page ->
            // Image with icons overlayed
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(model = page, contentDescription = "")

            }
        }

        // Dots indicator
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(images.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (isSelected) 10.dp else 8.dp)
                        .background(
                            color = if (isSelected) Color.Black else Color.Gray,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}





