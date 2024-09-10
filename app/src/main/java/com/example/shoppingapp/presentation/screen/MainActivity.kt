package com.example.shoppingapp.presentation.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.shoppingapp.domain.di.DiModel
import com.example.shoppingapp.ui.theme.lightPink11
import com.example.shoppingapp.ui.theme.statusBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val firebaseAuth = DiModel.provideFirebaseAuth()
        setContent {


                val navController = rememberNavController()
                window.statusBarColor = statusBar.toArgb()

                WindowCompat.getInsetsController(window, window.decorView)?.apply {
                    isAppearanceLightStatusBars = true
                }


                WindowCompat.getInsetsController(window, window.decorView).apply {
                    window.navigationBarColor = lightPink11.toArgb()
                    isAppearanceLightStatusBars = false
                }

                Scaffold {
                    Box(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
                       App(navController = navController, firebaseAuth = firebaseAuth)

                    }
                }

            }

        }
    }
