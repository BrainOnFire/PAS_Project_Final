package com.lzg.pas_project_final

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lzg.pas_project_final.ui.theme.PAS_Project_FinalTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PAS_Project_FinalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()){
                    Navigation()
                    //SignInScreen(navController = rememberNavController())
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback
            (true) {
                override fun handleOnBackPressed() {}
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Handle configuration changes here if needed (e.g.,adjust layout)
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "SignInScreen")
    {
        composable("SignInScreen") { SignInScreen(navController) }
        composable("MapScreen") { MapScreen(navController) }
        composable("ProfileScreen") { ProfileScreen() }
    }
}