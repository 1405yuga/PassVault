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
import com.example.passvault.utils.helper.VaultIconsList
import com.example.passvault.utils.state.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VaultHomeViewModel @Inject constructor(private val vaultRepository: VaultRepository) :
    ViewModel() {

    private var _vaultListScreenState =
        MutableStateFlow<ScreenState<List<Vault>>>(ScreenState.PreLoad())
    val vaultScreenState: StateFlow<ScreenState<List<Vault>>> = _vaultListScreenState

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

    init {
        getVaults(isInitialLoad = true)
    }

    fun getVaults(isInitialLoad: Boolean? = null) {
        _vaultListScreenState.value = ScreenState.Loading()
        try {
            viewModelScope.launch {
                val result = vaultRepository.getAllVaults()
                Log.d(this@VaultHomeViewModel.javaClass.simpleName, "Vaults : ${result.size}")

                if (result.isNotEmpty()) {
                    when (isInitialLoad) {
                        true -> onMenuSelected(
                            navDrawerMenus = NavDrawerMenus.VaultItem(vault = result.first())
                        )

                        false -> onMenuSelected(
                            navDrawerMenus = NavDrawerMenus.VaultItem(vault = result.last())
                        )

                        null -> {
                            if (result.size == 1) {
                                onMenuSelected(
                                    navDrawerMenus = NavDrawerMenus.VaultItem(vault = result.first())
                                )
                            }
                        }
                    }
                }
                _vaultListScreenState.value = ScreenState.Loaded(result = result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _vaultListScreenState.value = ScreenState.Error(message = "Unable to load Vaults")
        }
    }

    //Add Vault-------------------------------------------------------------------------------
    var openAddVaultDialog by mutableStateOf(false)
        private set

    fun toggleCreateVaultDialog(openDialog: Boolean) {
        this.openAddVaultDialog = openDialog
        if (openDialog == false) resetDialogState()
    }

    var addVaultDialogState by mutableStateOf(AddVaultDialogState())
        private set

    fun onVaultNameChange(vaultName: String) {
        addVaultDialogState = addVaultDialogState.copy(vaultName = vaultName)
    }

    fun onIconSelected(icon: ImageVector) {
        addVaultDialogState = addVaultDialogState.copy(iconSelected = icon)
    }

    private fun updateDialogScreenState(state: ScreenState<String>) {
        addVaultDialogState = addVaultDialogState.copy(screenState = state)
    }

    private fun checkInputFields(): Boolean {
        addVaultDialogState = addVaultDialogState.copy(
            vaultError =
                if (addVaultDialogState.vaultName.trim().isBlank()) "Vault Name is required"
                else ""
        )
        return addVaultDialogState.vaultError.isBlank()
    }

    private fun resetDialogState() {
        addVaultDialogState = AddVaultDialogState()
    }

    fun addNewVault() {
        if (!checkInputFields()) return
        updateDialogScreenState(ScreenState.Loading())
        viewModelScope.launch {
            updateDialogScreenState(
                state = try {
                    vaultRepository.insertVault(
                        vault = Vault(
                            vaultName = this@VaultHomeViewModel.addVaultDialogState.vaultName,
                            iconKey = this@VaultHomeViewModel.addVaultDialogState.iconSelected.name
                        )
                    )
                    getVaults(isInitialLoad = false)
                    toggleCreateVaultDialog(openDialog = false)
                    ScreenState.Loaded("Vault Added")
                } catch (e: Exception) {
                    e.printStackTrace()
                    ScreenState.Error(message = "Unable to add vault")
                }
            )
        }
    }

    //Remove Vault----------------------------------------------------------------

    var openRemoveVaultConfirmationDialog by mutableStateOf(false)
        private set

    var vaultToBeRemoved: Vault? by mutableStateOf(null)
        private set

    fun showRemoveConfirmation(vault: Vault) {
        this.vaultToBeRemoved = vault
        this.openRemoveVaultConfirmationDialog = true
    }

    fun closeRemoveDialog() {
        this.vaultToBeRemoved = null
        this.openRemoveVaultConfirmationDialog = false
    }

    fun removeVault() {
        try {
            if (vaultToBeRemoved != null) {
                viewModelScope.launch {
                    val result = vaultRepository.deleteVault(vaultId = vaultToBeRemoved!!.vaultId)
                    closeRemoveDialog()
                    result.onSuccess {
                        getVaults()
                        Log.d(this@VaultHomeViewModel.javaClass.simpleName, "Vault deleted")
                    }.onFailure { exception ->
                        exception.printStackTrace()
                    }
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

data class AddVaultDialogState(
    val screenState: ScreenState<String> = ScreenState.PreLoad(),
    val vaultName: String = "",
    val vaultError: String = "",
    val iconSelected: ImageVector = VaultIconsList.getIconsList()[0]
)