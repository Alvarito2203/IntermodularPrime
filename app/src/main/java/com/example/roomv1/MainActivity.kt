// Archivo: MainActivity.kt
package com.example.roomv1

import AppNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController

import com.example.roomv1.room.FacturaDatabaseDao
import com.example.roomv1.ui.theme.Roomv1Theme
import com.example.roomv1.viewmodels.FacturasViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = FacturaDatabaseDao()
        val viewModel = FacturasViewModel(dao)

        setContent {
            Roomv1Theme {
                Surface(color = Color.White) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}
