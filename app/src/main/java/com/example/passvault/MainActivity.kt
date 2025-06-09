package com.example.passvault

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
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
import com.example.passvault.ui.screens.main.MainScreen
import com.example.passvault.ui.theme.PassVaultTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PassVaultTheme {
                Surface {
                    PassVaultApp()
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
        startDestination = Screen.Loader.name,
        modifier = modifier.windowInsetsPadding(WindowInsets.systemBars)
    ) {
        composable(Screen.Loader.name) {
            LoaderScreen(
                toLoginScreen = { navController.navigateAndClearPrevious(Screen.Login.name) },
                toCreateMasterKeyScreen = { navController.navigateAndClearPrevious(Screen.CreateMasterKey.name) },
                toEnterMasterKeyScreen = { navController.navigateAndClearPrevious(Screen.EnterMasterKey.name) })
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
            EnterMasterKeyScreen(
                viewModel = hiltViewModel(),
                onUnlocked = { navController.navigateAndClearPrevious(Screen.MainScreen.name) })
        }
        composable(Screen.MainScreen.name) {
            MainScreen(mainScreenViewModel = hiltViewModel())
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
    Login, SignUp, Loader, CreateMasterKey, EnterMasterKey, MainScreen
}