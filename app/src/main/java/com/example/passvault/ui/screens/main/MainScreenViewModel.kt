package com.example.passvault.ui.screens.main

import androidx.lifecycle.ViewModel

class MainScreenViewModel : ViewModel() {

}

sealed class MainScreens(val route: String) {
    object VaultHome : MainScreens(route = "VaultHome")
    object AddVault : MainScreens(route = "Add Vault")
    object AddPassword : MainScreens(route = "AddPassword")
    object Profile : MainScreens(route = "Profile")
}