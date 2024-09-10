package com.example.shoppingapp.presentation.screen.loginSignUp

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.common.UserData
import com.example.shoppingapp.presentation.navigation.Routs
import com.example.shoppingapp.presentation.navigation.SubNavigation
import com.example.shoppingapp.presentation.viewmodel.ViewModel
import com.example.shoppingapp.ui.theme.backGraund
import com.example.shoppingapp.ui.theme.backGraundCard
import com.example.shoppingapp.ui.theme.statusBar


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ViewModel = hiltViewModel(),
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginState by viewModel.loginScreenState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backGraund)
    ) {
        when {
            loginState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            !loginState.userData.isNullOrEmpty() -> {
                LaunchedEffect(Unit) {
                    navController.navigate(SubNavigation.MainHomeScreen)
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    loginState.error?.let {
                        Text(text = it)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        label = { Text(text = "Email") },
                        value = email,
                        onValueChange = { email = it }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        label = { Text(text = "Password") },
                        value = password,
                        onValueChange = { password = it }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        modifier = Modifier.width(300.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = statusBar
                        ),
                        onClick = {
                            viewModel.loginUser(UserData(email = email, password = password))
                        }
                    ) {
                        Text(text = "Login")
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text(text = "Not have an account? ")
                        Text(
                            modifier = Modifier.clickable {
                                navController.navigate(Routs.SignUpScreenRout)
                            },
                            text = "Sign Up",
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold,
                            color = Color.Blue
                        )
                    }
                }
            }
        }
    }
}
