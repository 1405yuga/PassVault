package com.example.passvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passvault.ui.screens.authentication.AuthViewModel
import com.example.passvault.ui.screens.authentication.Login
import com.example.passvault.ui.screens.authentication.SignUp
import com.example.passvault.ui.screens.main_screens.Home
import com.example.passvault.ui.theme.PassVaultTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PassVaultTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PassVaultApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PassVaultApp(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
    val navController = rememberNavController()
    val userDetails by authViewModel.userInfo.collectAsState()

    LaunchedEffect(userDetails) {
        if (userDetails != null) navController.navigateAndClearPrevious(Screen.Home.name)
    }

    NavHost(
        navController = navController,
        startDestination = if (userDetails != null) Screen.Home.name else Screen.Login.name
    ) {
        composable(Screen.Login.name) {
            Login(
                onLoginClick = {
                    navController.navigateAndClearPrevious(Screen.Home.name)
                },
                onSignUpClick = {
                    navController.navigateAndClearPrevious(Screen.SignUp.name)
                },
                modifier = modifier,
                authViewModel = authViewModel
            )
        }
        composable(Screen.Home.name) {
            Home(modifier = modifier)
        }
        composable(Screen.SignUp.name) {
            SignUp(
                navToLogin = { navController.navigateAndClearPrevious(Screen.Login.name) },
                authViewModel = authViewModel
            )
        }
    }

}

fun NavController.navigateAndClearPrevious(route: String) {
    this.navigate(route) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

@Composable
@Preview
fun PassVaultAppPreview() {
    PassVaultApp()
}

enum class Screen {
    Home, Login, SignUp
}