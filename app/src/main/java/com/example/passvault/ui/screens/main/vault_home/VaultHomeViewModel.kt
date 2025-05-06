package com.example.passvault.ui.screens.main.vault_home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.example.passvault.data.Vault
import com.example.passvault.utils.extension_functions.toOutlinedIcon

class VaultHomeViewModel : ViewModel() {
    val dummyVaultList = listOf(
        Vault(
            vaultId = 0L,
            userId = "123",
            vaultName = "Personal",
            iconKey = "Home",
            createdAt = ""
        ),
        Vault(
            vaultId = 0L,
            userId = "123",
            vaultName = "Websites",
            iconKey = "Language",
            createdAt = ""
        ),
        Vault(
            vaultId = 0L,
            userId = "123",
            vaultName = "Favourites",
            iconKey = "Favorite",
            createdAt = ""
        ),
        Vault(
            vaultId = 0L,
            userId = "123",
            vaultName = "Important",
            iconKey = "Lock",
            createdAt = "",
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

    var showCreateVaultDialog by mutableStateOf(false)
        private set

    fun toggleCreateVaultDialog(showDialog: Boolean) {
        this.showCreateVaultDialog = showDialog
    }

}

sealed class NavDrawerMenus(val label: String, val icon: ImageVector) {
    data class VaultItem(val vault: Vault) :
        NavDrawerMenus(label = vault.vaultName, icon = vault.iconKey.toOutlinedIcon())

    object AddVault : NavDrawerMenus(label = "Add Vault", icon = Icons.Outlined.Add)
    object Profile : NavDrawerMenus(label = "Profile", icon = Icons.Filled.Person)
}
