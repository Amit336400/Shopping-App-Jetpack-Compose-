package com.example.shoppingapp.presentation.screen.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shoppingapp.presentation.navigation.Routs
import com.example.shoppingapp.ui.theme.backGraund

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHome(
    navController: NavController
) {
    var profileName = remember {
        mutableStateOf("Amit")
    }
    Scaffold(
//        topBar = {
//            TopAppBar( colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = backGraund
//            ),
//                //title = { Text(text = " Hey! ${profileName.value}") }
//            )
//        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        innerPadding
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .background(backGraund)
        ) {

            ///
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp, start = 20.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(
                    containerColor = backGraund
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Box(
                        modifier = Modifier.clickable {

                        }
                            .border(width = 1.dp, color = Color.Blue)
                            .padding(15.dp)
                            .fillMaxWidth(0.4f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                imageVector = Icons.Default.MailOutline,
                                contentDescription = null
                            )
                            Text(text = "Order")
                        }
                    }

                    Box(
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.Blue)
                            .padding(15.dp)
                            .fillMaxWidth(0.78f)
                            .clickable {
                                navController.navigate(Routs.EditProfile)
                            }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                imageVector = Icons.Default.Face,
                                contentDescription = null
                            )
                            Text(text = "Profile")
                        }
                    }


                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Box(
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.Blue)
                            .padding(15.dp)
                            .fillMaxWidth(0.4f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = null
                            )
                            Text(text = "Wishlist")
                        }
                    }

                    Box(
                        modifier = Modifier
                            .border(width = 1.dp, color = Color.Blue)
                            .padding(15.dp)
                            .fillMaxWidth(0.78f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                imageVector = Icons.Default.Call,
                                contentDescription = null
                            )
                            Text(text = "Help")
                        }
                    }
                }
            }

            //---------Account Setting ----\\
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    text = "Account Setting"
                )
                Spacer(modifier = Modifier.height(20.dp))

                /*
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Edit Profile")

                }

                Spacer(modifier = Modifier.height(20.dp))
                */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(imageVector = Icons.Default.LocationOn, contentDescription = null)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Save Address")

                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(imageVector = Icons.Default.Notifications, contentDescription = null)
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Notification Setting")

                }


            }
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /*TODO*/ }) {
                    Text(text = "Log Out")
                }
            }
        }
    }
}