package com.example.passvault.ui.screens.main.vault_home

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.data.Vault
import com.example.passvault.network.supabase.VaultRepository
import com.example.passvault.utils.extension_functions.toOutlinedIcon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaultHomeViewModel @Inject constructor(private val vaultRepository: VaultRepository) :
    ViewModel() {

    private val _vaults = MutableStateFlow<List<Vault>>(emptyList())
    val vaults: StateFlow<List<Vault>> = _vaults

    var currentSelectedMenu by mutableStateOf<NavDrawerMenus?>(null)
        private set

    var lastVaultMenu by mutableStateOf<NavDrawerMenus?>(currentSelectedMenu)
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

    init {
        getVaults()
    }

    fun getVaults() {
        try {
            viewModelScope.launch {
                val result = vaultRepository.getAllVaults()
                _vaults.value = result
                Log.d(this@VaultHomeViewModel.javaClass.simpleName, "Vaults : ${result.size}")

                if (result.isNotEmpty() && currentSelectedMenu == null) {
                    onMenuSelected(NavDrawerMenus.VaultItem(result[0]))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

sealed class NavDrawerMenus(val label: String, val icon: ImageVector) {
    data class VaultItem(val vault: Vault) :
        NavDrawerMenus(label = vault.vaultName, icon = vault.iconKey.toOutlinedIcon())

    object AddVault : NavDrawerMenus(label = "Add Vault", icon = Icons.Outlined.Add)
    object Profile : NavDrawerMenus(label = "Profile", icon = Icons.Filled.Person)
}
