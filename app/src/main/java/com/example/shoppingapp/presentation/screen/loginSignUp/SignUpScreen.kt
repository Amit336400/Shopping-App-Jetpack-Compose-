package com.example.shoppingapp.presentation.screen.loginSignUp

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.shoppingapp.common.UserData
import com.example.shoppingapp.presentation.navigation.Routs
import com.example.shoppingapp.presentation.navigation.SubNavigation
import com.example.shoppingapp.presentation.viewmodel.ViewModel
import com.example.shoppingapp.ui.theme.backGraund
import com.example.shoppingapp.ui.theme.statusBar


@SuppressLint("SuspiciousIndentation")
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: ViewModel = hiltViewModel(),
    navController: NavController,
) {

    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confPassword = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }

    val state = viewModel.signUpScreenState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backGraund)
    ) {
        if (state.value.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backGraund),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()

            }
        }
        else if (!state.value.userData.isNullOrEmpty()) {
            LaunchedEffect(key1 = true){
                viewModel.addUserDataFromDB(
                    userData = UserData(
                        name = name.value,
                        email = email.value,
                        password = password.value,
                        phone = phoneNumber.value
                    )

                )
            }
            val state1 = viewModel.userAddResult.collectAsStateWithLifecycle().value
            if (!state1.userData.isNullOrEmpty()){
                LaunchedEffect(Unit) {
                    navController.navigate(SubNavigation.MainHomeScreen)
                }
            }else if (state1.isLoading){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backGraund),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator()

                }
            }
        } else {
            Column(modifier = modifier
                .padding(20.dp)
                .background(backGraund)) {
                Text(text = "Sign Up", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                //*       if Any Error        *
                if (!state.value.error.isNullOrEmpty()) {
                    Text(
                        fontWeight = FontWeight.ExtraBold, fontSize = 20.sp,
                        color = Color.Red,
                        text = "${state.value.error}"
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))
                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Name") },
                    value = name.value, onValueChange = { name.value = it })

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Email") },
                    value = email.value, onValueChange = { email.value = it })

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Phone Number") },
                    value = phoneNumber.value, onValueChange = { phoneNumber.value = it })

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Password") },
                    value = password.value, onValueChange = { password.value = it })

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Confirm Password") },
                    value = confPassword.value, onValueChange = { confPassword.value = it })

                Spacer(modifier = Modifier.height(20.dp))

                //SignUp Button Click to Sign Up Method Call
                Button(modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = statusBar
                    ),
                    onClick = {
                        if (emptyValidation(
                                name.value,
                                email.value,
                                confPassword.value,
                                password.value,
                                phoneNumber.value
                            )
                        ) {
                            //Call Login Function
                            viewModel.createUser(
                                UserData(
                                    name = name.value,
                                    email = email.value,
                                    password = password.value,
                                    phone = phoneNumber.value
                                )
                            )
                            Log.d("TAG", "SignUpScreen:")
                        } else {
                            //If Text Box Is Empty
                        }
                    }) {
                    Text(text = " Sign Up")
                }

                Spacer(modifier = Modifier.height(20.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(text = "Already have an account? ")
                    Text(
                        // Login Text Click TO go Login Page
                        modifier = Modifier.clickable {
                            navController.navigate(Routs.LoginScreenRout)
                        },
                        text = " Login",
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                //SignUp With Google Button
                OutlinedButton(
                    onClick = {

                    },
                    modifier.fillMaxWidth()
                ) {
                    Row(horizontalArrangement = Arrangement.Center) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                        Spacer(modifier = modifier.width(30.dp))
                        Text(text = "Sign Up with Google")
                    }

                }

            }
        }
    }

}

// Check the TextBox Is Empty Or Not && Confirm Password == Password

fun emptyValidation(nam: String, em: String, cp: String, p: String, ph: String): Boolean {
    if (nam.isNotEmpty() || em.isNotEmpty() || cp.isNotEmpty() || p.isNotEmpty() || ph.isNotEmpty()) {
        if (cp == p) {
            return true
        } else {
            return false
        }
    } else {
        return false
    }
}



