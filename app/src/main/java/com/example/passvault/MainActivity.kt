package com.example.passvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passvault.ui.screens.MasterKeyScreen
import com.example.passvault.ui.screens.authentication.login.Login
import com.example.passvault.ui.screens.authentication.signup.SignUp
import com.example.passvault.ui.screens.loader.LoaderScreen
import com.example.passvault.ui.screens.main_screens.Home
import com.example.passvault.ui.theme.PassVaultTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

    ) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Loader.name
    ) {
        composable(Screen.Loader.name) {
            LoaderScreen(
                toLoginScreen = { navController.navigateAndClearPrevious(Screen.Login.name) },
                toMasterKeyScreen = { navController.navigateAndClearPrevious(Screen.MasterKey.name) },
                toHomeScreen = { navController.navigateAndClearPrevious(Screen.Home.name) }
            )
        }
        composable(Screen.Home.name) {
            Home(
                onLogoutClick = {},
                modifier = modifier
            )
        }
        composable(Screen.Login.name) {
            Login(
                onLoginClick = { navController.navigateAndClearPrevious(Screen.Loader.name) },
                onSignUpClick = { navController.navigateAndClearPrevious(Screen.SignUp.name) },
                modifier = modifier
            )
        }
        composable(Screen.SignUp.name) {
            SignUp(
                navToLogin = { navController.navigateAndClearPrevious(Screen.Loader.name) },
            )
        }
        composable(Screen.MasterKey.name) {
            MasterKeyScreen()
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
    Home, Login, SignUp, Loader, MasterKey
}