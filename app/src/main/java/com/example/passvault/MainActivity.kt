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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passvault.ui.screens.authentication.create_masterkey.CreateMasterKeyScreen
import com.example.passvault.ui.screens.authentication.enter_masterkey.EnterMasterKeyScreen
import com.example.passvault.ui.screens.authentication.login.LoginScreen
import com.example.passvault.ui.screens.authentication.signup.SignUpScreen
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
        navController = navController, startDestination = Screen.Loader.name
    ) {
        composable(Screen.Loader.name) {
            LoaderScreen(
                toLoginScreen = { navController.navigateAndClearPrevious(Screen.Login.name) },
                toCreateMasterKeyScreen = { navController.navigateAndClearPrevious(Screen.CreateMasterKey.name) },
                toEnterMasterKeyScreen = { navController.navigateAndClearPrevious(Screen.EnterMasterKey.name) })
        }
        composable(Screen.Home.name) {
            Home(
                onLogoutClick = {}, modifier = modifier
            )
        }
        composable(Screen.Login.name) {
            LoginScreen(
                onLoginClick = { navController.navigateAndClearPrevious(Screen.Loader.name) },
                onSignUpClick = { navController.navigateAndClearPrevious(Screen.SignUp.name) },
                viewModel = hiltViewModel(),
                modifier = modifier
            )
        }
        composable(Screen.SignUp.name) {
            SignUpScreen(
                navToLogin = { navController.navigateAndClearPrevious(Screen.Loader.name) },
                viewModel = hiltViewModel()
            )
        }
        composable(Screen.CreateMasterKey.name) {
            CreateMasterKeyScreen(
                onConfirmClick = { navController.navigateAndClearPrevious(Screen.Loader.name) },
                viewModel = hiltViewModel()
            )
        }
        composable(Screen.EnterMasterKey.name) {
            EnterMasterKeyScreen(viewModel = hiltViewModel())
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
    Home, Login, SignUp, Loader, CreateMasterKey, EnterMasterKey
}