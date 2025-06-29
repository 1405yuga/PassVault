package com.example.passvault.ui.screens.main.nav_drawer

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.data.Vault
import com.example.passvault.di.shared_reference.MasterCredentialsRepository
import com.example.passvault.network.supabase.AuthRepository
import com.example.passvault.network.supabase.EncryptedDataRepository
import com.example.passvault.network.supabase.VaultRepository
import com.example.passvault.utils.extension_functions.toImageVector
import com.example.passvault.utils.helper.VaultIconsList
import com.example.passvault.utils.state.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavMenusViewModel @Inject constructor(
    private val vaultRepository: VaultRepository,
    private val encryptedDataRepository: EncryptedDataRepository,
    private val masterCredentialsRepository: MasterCredentialsRepository,
    private val authRepository: AuthRepository
) :
    ViewModel() {

    //    SignOut-------------------------------------------------------------------------
    var isVisibleSignOutConfirmationDialog by mutableStateOf(false)
        private set

    fun showSignOutConfirmation() {
        this.isVisibleSignOutConfirmationDialog = true
    }

    fun hideSignOutConfirmation() {
        this.isVisibleSignOutConfirmationDialog = false
    }

    private val _signOutState = MutableStateFlow<ScreenState<String>>(ScreenState.PreLoad())
    val signOutState: StateFlow<ScreenState<String>> = _signOutState

    //remove locally stored master-credentials
    //sign-out
    fun performSignOut() {
        _signOutState.value = ScreenState.Loading()
        viewModelScope.launch {
            _signOutState.value = try {
                masterCredentialsRepository.saveMasterCredentialsJsonLocally(masterCredentionsJson = null)
                authRepository.signOut()
                ScreenState.Loaded("Signed out! Bye!")
            } catch (e: Exception) {
                e.printStackTrace()
                ScreenState.Error("Unable to Sign-Out")
            }
        }
    }

//Add Vault-------------------------------------------------------------------------------

    private val _addDialogScreenState = MutableStateFlow<ScreenState<Vault>>(ScreenState.PreLoad())
    val addDialogScreenState: StateFlow<ScreenState<Vault>> = _addDialogScreenState

    var openAddVaultDialog by mutableStateOf(false)
        private set

    var openedVault by mutableStateOf<Vault?>(null)
        private set

    fun toggleCreateVaultDialog(openDialog: Boolean, vault: Vault? = null) {
        if (!openDialog) {
            resetDialogState()
            openAddVaultDialog = false
        } else {
            resetDialogState() // force re-init state on every open
            setEditableVaultDialogState(vault)
            openAddVaultDialog = true
        }
    }

    var vaultName by mutableStateOf<String>("")
        private set

    var vaultError by mutableStateOf<String>("")
        private set

    var iconSelected by mutableStateOf<ImageVector?>(null)
        private set
    private var vaultId by mutableStateOf<Long?>(null)

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
        this.openedVault = null
        this.vaultId = null
        this._addDialogScreenState.value = ScreenState.PreLoad()
        this._removeVaultScreenState.value = ScreenState.PreLoad()
    }

    private fun setEditableVaultDialogState(vault: Vault?) {
        this.openedVault = vault
        vault?.let {
            this.vaultId = vault.vaultId
            this.vaultName = vault.vaultName
            this.iconSelected = vault.iconKey.toImageVector()
        }
    }

    fun addNewVault() {
        if (!checkInputFields()) return
        _addDialogScreenState.value = ScreenState.Loading()
        viewModelScope.launch {
            try {
                var resultVault: Vault? = Vault(
                    vaultId = this@NavMenusViewModel.vaultId,
                    vaultName = this@NavMenusViewModel.vaultName,
                    iconKey = this@NavMenusViewModel.iconSelected?.name
                        ?: VaultIconsList.getIconsList()[0].name
                )

                resultVault = vaultRepository.upsertVault(vault = resultVault!!)
                if (resultVault != null) {
                    _addDialogScreenState.value = ScreenState.Loaded(resultVault)
                } else {
                    _addDialogScreenState.value = ScreenState.Error(message = "Unable to add vault")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _addDialogScreenState.value = ScreenState.Error(message = "Unable to add vault")
            }
        }
    }

//Remove Vault----------------------------------------------------------------

//    var openRemoveVaultConfirmationDialog by mutableStateOf(false)
//        private set
//
//    var vaultToBeRemoved: Vault? by mutableStateOf(null)
//        private set

//    fun showRemoveConfirmation(vault: Vault) {
//        this.vaultToBeRemoved = vault
//        this.openRemoveVaultConfirmationDialog = true
//    }
//
//    fun closeRemoveDialog() {
//        this.vaultToBeRemoved = null
//        this.openRemoveVaultConfirmationDialog = false
//    }

    private val _removeVaultScreenState =
        MutableStateFlow<ScreenState<Long>>(ScreenState.PreLoad())
    val removeVaultScreenState: StateFlow<ScreenState<Long>> = _removeVaultScreenState

    fun removeVault() {
        _removeVaultScreenState.value = ScreenState.Loading()
        try {
            if (openedVault != null) {
                viewModelScope.launch {
                    val result = vaultRepository.deleteVault(vaultId = openedVault!!.vaultId)
                    result.onSuccess {
                        Log.d(this@NavMenusViewModel.javaClass.simpleName, "Vault deleted")
                        _removeVaultScreenState.value = ScreenState.Loaded(openedVault!!.vaultId!!)
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

//    //Delete password------------------------------------------------------------------------
//    var deletePasswordConfirmationDialog by mutableStateOf(false)
//        private set
//
//    private var passwordIdToBeRemoved: Long? = null
//
//    fun showDeletePasswordConfirmation(passwordId: Long?) {
//        if (passwordId != null) {
//            this.passwordIdToBeRemoved = passwordId
//            this.deletePasswordConfirmationDialog = true
//        }
//    }
//
//    fun hideDeletePasswordConfirmation() {
//        this.deletePasswordConfirmationDialog = false
//        this.passwordIdToBeRemoved = null
//    }
//
//    private val _deletePasswordScreenState =
//        MutableStateFlow<ScreenState<Long>>(ScreenState.PreLoad())
//    val deletePasswordScreenState: StateFlow<ScreenState<Long>> = _deletePasswordScreenState
//
//    fun deletePassword() {
//        if (this.passwordIdToBeRemoved == null) return
//        _deletePasswordScreenState.value = ScreenState.Loading()
//        viewModelScope.launch {
//            _deletePasswordScreenState.value = try {
//                val result =
//                    encryptedDataRepository.deleteEncryptedDataById(passwordId = this@NavMenusViewModel.passwordIdToBeRemoved!!)
//                if (result.isSuccess) ScreenState.Loaded(passwordIdToBeRemoved!!)
//                else ScreenState.Error("Something went wrong")
//            } catch (e: Exception) {
//                e.printStackTrace()
//                ScreenState.Error("Unable to delete")
//            }
//        }
//    }
}

sealed class NavDrawerMenus(val label: String, val icon: ImageVector) {
    data class VaultItem(val vault: Vault) :
        NavDrawerMenus(label = vault.vaultName, icon = vault.iconKey.toImageVector())

    object AddVault : NavDrawerMenus(label = "Add Vault", icon = Icons.Outlined.Add)
    object Profile : NavDrawerMenus(label = "Profile", icon = Icons.Filled.Person)
    object LogOut : NavDrawerMenus(label = "Log Out", icon = Icons.Outlined.PowerSettingsNew)
}