package com.example.passvault.ui.screens.main.nav_drawer

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
import com.example.passvault.data.EncryptedData
import com.example.passvault.data.Vault
import com.example.passvault.network.supabase.EncryptedDataRepository
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
class VaultHomeViewModel @Inject constructor(
    private val vaultRepository: VaultRepository,
    private val encryptedDataRepository: EncryptedDataRepository
) :
    ViewModel() {

    //List Screen-------------------------------------------------------------------------
    private val _passwordListScreenState =
        MutableStateFlow<ScreenState<List<EncryptedData>>>(ScreenState.PreLoad())

    val passwordListScreenState: StateFlow<ScreenState<List<EncryptedData>>> =
        _passwordListScreenState

    fun fetchEncryptedData(vaultId: Long?) {
        _passwordListScreenState.value = ScreenState.Loading()
        viewModelScope.launch {
            _passwordListScreenState.value = try {
                if (vaultId != null) {
                    val result =
                        encryptedDataRepository.getAllEncryptedDataAtVaultId(vaultId = vaultId)
                    if (result == null) {
                        ScreenState.Error("Unable to load Passwords list")
                    } else {
                        ScreenState.Loaded(result)
                    }
                } else {
                    ScreenState.Error("Vault Not found")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                ScreenState.Error("Unable to load Passwords list")
            }
        }
    }

    //Add Vault-------------------------------------------------------------------------------

    private val _addDialogScreenState = MutableStateFlow<ScreenState<Vault>>(ScreenState.PreLoad())
    val addDialogScreenState: StateFlow<ScreenState<Vault>> = _addDialogScreenState

    var openAddVaultDialog by mutableStateOf(false)
        private set

    fun toggleCreateVaultDialog(openDialog: Boolean) {
        this.openAddVaultDialog = openDialog
        if (openDialog == false) resetDialogState()
    }

    var vaultName by mutableStateOf<String>("")
        private set

    var vaultError by mutableStateOf<String>("")
        private set

    var iconSelected by mutableStateOf<ImageVector?>(null)
        private set

    fun onVaultNameChange(vaultName: String) {
        this.vaultName = vaultName
    }

    fun onIconSelected(icon: ImageVector?) {
        this.iconSelected = icon
    }

    private fun checkInputFields(): Boolean {
        this.vaultError =
            if (this.vaultName.trim().isBlank()) "Vault Name is required"
            else ""
        return this.vaultError.isBlank()
    }

    private fun resetDialogState() {
        onVaultNameChange(vaultName = "")
        onIconSelected(null)
        this.vaultError = ""
        this._addDialogScreenState.value = ScreenState.PreLoad()
    }

    fun addNewVault() {
        if (!checkInputFields()) return
        _addDialogScreenState.value = ScreenState.Loading()
        viewModelScope.launch {
            try {
                val resultVault = Vault(
                    vaultName = this@VaultHomeViewModel.vaultName,
                    iconKey = this@VaultHomeViewModel.iconSelected?.name
                        ?: VaultIconsList.getIconsList()[0].name
                )

                vaultRepository.insertVault(vault = resultVault)
                _addDialogScreenState.value = ScreenState.Loaded(resultVault)
            } catch (e: Exception) {
                e.printStackTrace()
                _addDialogScreenState.value = ScreenState.Error(message = "Unable to add vault")
            }
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

    private val _removeVaultScreenState =
        MutableStateFlow<ScreenState<Vault>>(ScreenState.PreLoad())
    val removeVaultScreenState: StateFlow<ScreenState<Vault>> = _removeVaultScreenState

    fun removeVault() {
        _removeVaultScreenState.value = ScreenState.Loading()
        try {
            if (vaultToBeRemoved != null) {
                viewModelScope.launch {
                    val result = vaultRepository.deleteVault(vaultId = vaultToBeRemoved!!.vaultId)
                    result.onSuccess {
                        Log.d(this@VaultHomeViewModel.javaClass.simpleName, "Vault deleted")
                        _removeVaultScreenState.value = ScreenState.Loaded(vaultToBeRemoved!!)
                    }.onFailure { exception ->
                        exception.printStackTrace()
                        _removeVaultScreenState.value = ScreenState.Error("Unable to delete Vault")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _removeVaultScreenState.value = ScreenState.Error("Unable to delete Vault")
        }
    }
}

sealed class NavDrawerMenus(val label: String, val icon: ImageVector) {
    data class VaultItem(val vault: Vault) :
        NavDrawerMenus(label = vault.vaultName, icon = vault.iconKey.toOutlinedIcon())

    object AddVault : NavDrawerMenus(label = "Add Vault", icon = Icons.Outlined.Add)
    object Profile : NavDrawerMenus(label = "Profile", icon = Icons.Filled.Person)
}