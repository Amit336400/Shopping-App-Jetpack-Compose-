package com.example.shoppingapp.presentation.screen.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shoppingapp.common.CategoryModel
import com.example.shoppingapp.common.ProductModel
import com.example.shoppingapp.common.calculatedPercentage
import com.example.shoppingapp.presentation.navigation.Routs
import com.example.shoppingapp.ui.theme.backGraund
import com.example.shoppingapp.ui.theme.backGraundCard

@Composable
fun Home(
    navigation: NavController,
    viewModel: homeViewModel = hiltViewModel(),
) {
    val catagory = viewModel.allCategory.collectAsStateWithLifecycle()
    val product = viewModel.allProduct.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backGraund)
    ) {
        //Category Row
        Row(modifier = Modifier.padding(top = 5.dp)) {

            if (catagory.value.isLoading) {
                isCategoryLoading()
            } else if (!(catagory.value.data.isNullOrEmpty())) {
                categoryData(catagory.value.data)
            } else if (!(catagory.value.error.isNullOrEmpty())) {
                errorCategory(catagory.value.error!!)
            }

        }

        //Banner Items Image Slider
//        Row(
//            modifier = Modifier
//                .height(100.dp)
//                .fillMaxWidth()
//                .background(Color.Cyan)
//        ) { }
        //Some Products

            if (product.value.isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize().background(backGraund),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else if (!(product.value.product.isNullOrEmpty())) {
                Column {
                    Text(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                        text = "Best Shell"
                    )
                    displayProduct(product.value.product, navigation = navigation)
                }
            }
            else if (!(product.value.error.isNullOrEmpty())) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "${product.value.error}")
                }
            }

        }


    }


@Composable
fun displayProduct(product: List<ProductModel>, navigation: NavController) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(product) {
            OutlinedCard(
                modifier = Modifier
                    .height(300.dp)
                    .padding(10.dp)
                    .fillMaxWidth(0.4f)
                    .clickable {
                        //product Click Display The Product
                        navigation.navigate(Routs.SingleProductScreen(productId = it.productId.toString()))
                    },
                colors = CardDefaults.cardColors(
                    containerColor = backGraundCard
                ),
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        AsyncImage(
                            modifier = Modifier.height(220.dp),
                            contentScale = ContentScale.FillWidth,
                            model = it.productPhoto, contentDescription = ""
                        )


                        Text(fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            text = "${it.productName}")
                        Text(
                            fontSize = 16.sp,
                            text = "₹ ${
                                calculatedPercentage(
                                    price = it.productPrize,
                                    percentage = it.productDiscaountInPersebtage
                                )
                            }"
                        )


                    }
                }

            }
        }
    }

}

@Composable
fun CategoryItem(
    ImageUrl: String, Category: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(end = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.LightGray, CircleShape)
        ) {
            AsyncImage(
                model = ImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
        }
        Text(Category, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun isCategoryLoading() {
    LazyRow(
        modifier = Modifier.fillMaxWidth().background(backGraund),
        contentPadding = PaddingValues(horizontal = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val list = listOf(0, 1, 2, 3, 4, 5)
        items(list) {
            CategoryItem(ImageUrl = "", Category = "")
        }

    }
}

@Composable
fun categoryData(data: List<CategoryModel>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth().background(backGraund),
        contentPadding = PaddingValues(horizontal = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(data) {
            CategoryItem(ImageUrl = it.uri, Category = it.categoryName)
        }

    }
}

@Composable
fun errorCategory(error: String) {
    LazyRow(
        modifier = Modifier.fillMaxWidth().background(backGraund),
        contentPadding = PaddingValues(horizontal = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        val list = listOf(0)
        items(list) {
            Text(text = error)
        }
    }
}