package com.example.passvault.ui.screens.main

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passvault.ui.screens.main.add.AddPasswordScreen
import com.example.passvault.ui.screens.main.profile.ProfileScreen
import com.example.passvault.ui.screens.main.vault_home.VaultHomeScreen
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MainScreens.VaultHome.route) {
        composable(route = MainScreens.VaultHome.route) {
            VaultHomeScreen(
                viewModel = hiltViewModel(),
                toProfileScreen = { navController.navigate(MainScreens.Profile.route) },
                toAddPasswordScreen = { navController.navigate(MainScreens.AddPassword.route) }
            )
        }
        composable(route = MainScreens.AddPassword.route) {
            AddPasswordScreen(
                onClose = { navController.popBackStack() },
                viewModel = viewModel(),
                modifier = Modifier
            )
        }
        composable(route = MainScreens.AddVault.route) {
            // TODO: create dialog screen
        }
        composable(route = MainScreens.Profile.route) {
            ProfileScreen()
        }

    }
}


@Composable
@VerticalScreenPreview
private fun MainScreenPreview() {
    MainScreen()
}

@Composable
@HorizontalScreenPreview
private fun MainScreenHorizontalPreview() {
    MainScreen()
}