package com.example.passvault.ui.screens.main

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.passvault.data.Vault
import com.example.passvault.data.VaultTable
import com.example.passvault.ui.screens.main.add_password.AddPasswordScreen
import com.example.passvault.ui.screens.main.profile.ProfileScreen
import com.example.passvault.ui.screens.main.vault_home.VaultHomeScreen
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MainScreens.VaultHome.route) {
        composable(route = MainScreens.VaultHome.route) {
            VaultHomeScreen(
                viewModel = hiltViewModel(),
                toProfileScreen = { navController.navigate(MainScreens.Profile.route) },
                toAddPasswordScreen = { vaultString ->
                    navController.navigate("${MainScreens.AddPassword.route}/$vaultString")
                }
            )
        }
        composable(
            route = "${MainScreens.AddPassword.route}/{${VaultTable.VAULT_JSON_ARGUMENT_NAME}}",
            arguments = listOf(navArgument(VaultTable.VAULT_JSON_ARGUMENT_NAME) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val selectedVault =
                navBackStackEntry.arguments?.getString(VaultTable.VAULT_JSON_ARGUMENT_NAME)
            selectedVault?.let {
                val vault = Gson().fromJson(it, Vault::class.java)
                AddPasswordScreen(
                    onClose = { navController.popBackStack() },
                    selectedVault = vault,
                    viewModel = hiltViewModel(),
                    modifier = Modifier
                )
                Log.d("MainScreen", "Vault received : $vault")
            }
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