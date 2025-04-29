package com.example.passvault.ui.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel

class MainScreenViewModel : ViewModel() {

    var currentSelectedMenu by mutableStateOf<NavDrawerMenus>(NavDrawerMenus.List)
        private set

//    var nonVaultMenu by mutableStateOf<NavDrawerMenus>(currentSelectedMenu)
//        private set

    fun onMenuSelected(navDrawerMenus: NavDrawerMenus) {
//        nonVaultMenu =
//            if ((navDrawerMenus !is NavDrawerMenus.AddVault) or (navDrawerMenus !is NavDrawerMenus.Profile)) {
//                currentSelectedMenu
//            } else {
//                navDrawerMenus
//            }
        currentSelectedMenu = navDrawerMenus
    }
}

sealed class NavDrawerMenus(val label: String, val icon: ImageVector) {
    object List : NavDrawerMenus(label = "List", icon = Icons.AutoMirrored.Outlined.List)
    object AddVault : NavDrawerMenus(label = "Add Vault", icon = Icons.Outlined.Add)
    object Profile : NavDrawerMenus(label = "Profile", icon = Icons.Filled.Person)
}

sealed class MainScreens(val route: String) {
    object VaultHome : MainScreens(route = "VaultHome")
    object AddVault : MainScreens(route = "Add Vault")
    object AddPassword : MainScreens(route = "AddPassword")
    object Profile : MainScreens(route = "Profile")
}