package com.example.passvault.ui.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.example.passvault.data.Vault

class MainScreenViewModel : ViewModel() {

    val dummyVaultList = listOf(
        Vault(
            vaultId = "1",
            userId = "123",
            vaultName = "Personal",
            imageVector = Icons.Default.Home
        ),
        Vault(
            vaultId = "2",
            userId = "123",
            vaultName = "Websites",
            imageVector = Icons.Default.Language
        ),
        Vault(
            vaultId = "1",
            userId = "123",
            vaultName = "Favourites",
            imageVector = Icons.Default.Favorite
        ),
        Vault(
            vaultId = "1",
            userId = "123",
            vaultName = "Important",
            imageVector = Icons.Default.Lock
        )

    )
    var currentSelectedMenu by mutableStateOf<NavDrawerMenus>(
        NavDrawerMenus.VaultItem(
            dummyVaultList[0]
        )
    )
        private set

    var lastVaultMenu by mutableStateOf<NavDrawerMenus>(currentSelectedMenu)
        private set

    fun onMenuSelected(navDrawerMenus: NavDrawerMenus) {
        if (navDrawerMenus is NavDrawerMenus.VaultItem) {
            lastVaultMenu = navDrawerMenus
        }
        currentSelectedMenu = navDrawerMenus
    }
}

sealed class NavDrawerMenus(val label: String, val icon: ImageVector) {
    data class VaultItem(val vault: Vault) :
        NavDrawerMenus(label = vault.vaultName, icon = vault.imageVector)

    object AddVault : NavDrawerMenus(label = "Add Vault", icon = Icons.Outlined.Add)
    object Profile : NavDrawerMenus(label = "Profile", icon = Icons.Filled.Person)
}

sealed class MainScreens(val route: String) {
    object VaultHome : MainScreens(route = "VaultHome")
    object AddVault : MainScreens(route = "Add Vault")
    object AddPassword : MainScreens(route = "AddPassword")
    object Profile : MainScreens(route = "Profile")
}