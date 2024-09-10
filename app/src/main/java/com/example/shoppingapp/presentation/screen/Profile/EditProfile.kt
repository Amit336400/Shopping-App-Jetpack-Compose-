package com.example.shoppingapp.presentation.screen.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shoppingapp.common.LoadingDialog
import com.example.shoppingapp.common.UserData
import com.example.shoppingapp.presentation.screen.Profile.profileViewModel.ProfileViewModel
import com.example.shoppingapp.presentation.viewmodel.ViewModel
import com.example.shoppingapp.ui.theme.backGraund

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EditProfile(
    viewModel: ProfileViewModel = hiltViewModel(),
    vm: ViewModel = hiltViewModel(),
) {
    val data = viewModel.userDataVal.collectAsStateWithLifecycle().value
    val addData = vm.userAddResult.collectAsStateWithLifecycle().value
    val res = data.data
    val isReadOnly = remember {
        mutableStateOf(false)
    }

    LoadingDialog(data.isLoading)
    LoadingDialog(addData.isLoading)
    
    if (!res.name.isNullOrEmpty()) {

        var name by remember {
            mutableStateOf(
                "${
                    if (!res.name.isNullOrEmpty()) res.name
                    else "Hello"
                }"
            )
        }
        var email by remember {
            mutableStateOf(
                "${
                    if (!res.email.isNullOrEmpty()) res.email
                    else "abc@gmail.com"
                }"
            )
        }
        var phNumber by remember {
            mutableStateOf(
                "${
                    if (!res.phone.isNullOrEmpty()) res.phone
                    else "000000000"
                }"
            )
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = backGraund
                    ),
                    title = { Text(text = "$name") })
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            innerPadding
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backGraund)
                    .padding(innerPadding)
            ) {


                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(backGraund),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(backGraund)
                    ) {
                        Card(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.Blue)
                                .height(100.dp)
                                .width(100.dp)
                                .align(Alignment.Center)
                        ) {
                            // Profile Photo
                            Box(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(backGraund),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ){
                                    Icon(modifier = Modifier.fillMaxSize(.7f),
                                        imageVector = Icons.Default.Person, contentDescription = "")

                                }
                            }
                        }
                    }


                    Column(modifier = Modifier
                        .padding(20.dp)
                        .background(backGraund)) {


                        Spacer(modifier = Modifier.height(30.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(text = "Name") },
                            value = name, onValueChange = { name = it }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(text = "Email") },
                            value = email, onValueChange = { email = it }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(text = "Phone Number") },
                            value = phNumber, onValueChange = { phNumber = it }
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                vm.addUserDataFromDB(
                                    UserData(
                                        name = name,
                                        email = email,
                                        phone = phNumber
                                    )
                                )
                            })
                        {
                            Text(text = "Save")

                        }
                        Spacer(modifier = Modifier.height(30.dp))


                    }


                }


            }

        }

    }
}