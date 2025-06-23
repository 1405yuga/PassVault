package com.example.passvault.ui.screens.main

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.passvault.data.PasswordDetailResult
import com.example.passvault.di.shared_reference.EncryptedPrefsModule
import com.example.passvault.di.shared_reference.MasterCredentialsRepository
import com.example.passvault.di.supabase.SupabaseModule
import com.example.passvault.network.supabase.EncryptedDataRepository
import com.example.passvault.network.supabase.VaultRepository
import com.example.passvault.ui.screens.main.add_password.UpsertPasswordDetailScreen
import com.example.passvault.ui.screens.main.nav_drawer.NavDrawerMenus
import com.example.passvault.ui.screens.main.nav_drawer.NavMenusScreen
import com.example.passvault.ui.screens.main.nav_drawer.profile.ProfileScreen
import com.example.passvault.ui.screens.main.view_password.ViewPasswordDetailScreen
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.example.passvault.utils.extension_functions.toVault
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainScreenViewModel: MainScreenViewModel, goToLoaderScreen: () -> Unit) {
    val navController = rememberNavController()
    val vaultList by mainScreenViewModel.vaultList.collectAsState()

    NavHost(navController = navController, startDestination = MainScreens.VaultHome.route) {
        composable(route = MainScreens.VaultHome.route) {
            NavMenusScreen(
                viewModel = hiltViewModel(),
                toProfileScreen = { navController.navigate(MainScreens.Profile.route) },
                toAddPasswordScreen = { navController.navigate(MainScreens.AddPassword.route) },
                toViewPasswordScreen = { passwordDetailResult ->
                    navController.navigate(
                        MainScreens.ViewPassword.createRoute(
                            passwordDetailsResultString = Gson().toJson(passwordDetailResult)
                        )
                    )
                },
                toEditPasswordScreen = { passwordDetailResult ->
                    navController.navigate(
                        MainScreens.AddPassword.createRoute(
                            passwordDetailsResultString = Gson().toJson(passwordDetailResult)
                        )
                    )
                },
                toLoaderScreen = { goToLoaderScreen() },
                mainScreenViewModel = mainScreenViewModel
            )
        }
        composable(
            route = MainScreens.AddPassword.route, arguments = listOf(
                navArgument(
                    MainScreens.AddPassword.ARGUMENT
                ) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
        ) { backStackEntry ->
            val passwordDetailResult: PasswordDetailResult? =
                try {
                    Gson().fromJson<PasswordDetailResult?>(
                        backStackEntry.arguments?.getString(MainScreens.AddPassword.ARGUMENT),
                        PasswordDetailResult::class.java
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            UpsertPasswordDetailScreen(
                onClose = { navController.popBackStack() },
                onSuccess = { passwordDetailResult ->
                    mainScreenViewModel.upsertPasswordToList(
                        passwordDetailResult = passwordDetailResult
                    )
                },
                onUpdateSelectedVault = { vault ->
                    mainScreenViewModel.onMenuSelected(
                        navDrawerMenus = NavDrawerMenus.VaultItem(vault = vault)
                    )
                },
                vaults = vaultList,
                initialSelectedVault = mainScreenViewModel.lastVaultMenu?.toVault(),
                passwordDetailResult = passwordDetailResult,
                viewModel = hiltViewModel(),
                modifier = Modifier
            )
        }
        composable(route = MainScreens.Profile.route) {
            ProfileScreen()
        }
        composable(
            route = MainScreens.ViewPassword.route,
            arguments = listOf(navArgument(MainScreens.ViewPassword.ARGUMENT) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val passwordDetailResult: PasswordDetailResult? =
                try {
                    Gson().fromJson<PasswordDetailResult?>(
                        backStackEntry.arguments?.getString(MainScreens.ViewPassword.ARGUMENT),
                        PasswordDetailResult::class.java
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            ViewPasswordDetailScreen(
                viewModel = hiltViewModel(),
                toEditPasswordScreen = {
                    //  pass and load data
                    navController.navigate(
                        MainScreens.AddPassword.createRoute(
                            passwordDetailsResultString = Gson().toJson(passwordDetailResult)
                        )
                    )
                },
                onClose = { navController.popBackStack() },
                passwordDetailsResult = passwordDetailResult!!
            )
        }
    }
}


@Composable
@VerticalScreenPreview
private fun MainScreenPreview() {
    MainScreen(
        mainScreenViewModel = MainScreenViewModel(
            vaultRepository = VaultRepository(SupabaseModule.mockClient),
            encryptedDataRepository = EncryptedDataRepository(SupabaseModule.mockClient),
            masterCredentialsRepository = MasterCredentialsRepository(EncryptedPrefsModule.mockSharedPreference),
        )
    ) {}
}

@Composable
@HorizontalScreenPreview
private fun MainScreenHorizontalPreview() {
    MainScreen(
        mainScreenViewModel = MainScreenViewModel(
            vaultRepository = VaultRepository(SupabaseModule.mockClient),
            encryptedDataRepository = EncryptedDataRepository(SupabaseModule.mockClient),
            masterCredentialsRepository = MasterCredentialsRepository(EncryptedPrefsModule.mockSharedPreference),
        )
    ) {}
}