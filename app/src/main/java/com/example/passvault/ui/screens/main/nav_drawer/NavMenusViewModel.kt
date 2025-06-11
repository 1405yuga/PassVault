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
import com.example.passvault.data.CipherEncodedBundle
import com.example.passvault.data.MasterCredentials
import com.example.passvault.data.PasswordDetailResult
import com.example.passvault.data.PasswordDetails
import com.example.passvault.data.Vault
import com.example.passvault.di.shared_reference.MasterCredentialsRepository
import com.example.passvault.network.supabase.EncryptedDataRepository
import com.example.passvault.network.supabase.VaultRepository
import com.example.passvault.utils.extension_functions.fromJsonString
import com.example.passvault.utils.extension_functions.toImageVector
import com.example.passvault.utils.helper.EncryptionHelper
import com.example.passvault.utils.helper.VaultIconsList
import com.example.passvault.utils.state.ScreenState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NavMenusViewModel @Inject constructor(
    private val vaultRepository: VaultRepository,
    private val encryptedDataRepository: EncryptedDataRepository,
    private val masterCredentialsRepository: MasterCredentialsRepository
) :
    ViewModel() {

    //List Screen-------------------------------------------------------------------------
    private val _passwordListScreenState =
        MutableStateFlow<ScreenState<List<PasswordDetailResult>>>(ScreenState.PreLoad())

    val passwordListScreenState: StateFlow<ScreenState<List<PasswordDetailResult>>> =
        _passwordListScreenState

    fun getPasswordsList(vaultId: Long?) {
        _passwordListScreenState.value = ScreenState.Loading()
        viewModelScope.launch {
            _passwordListScreenState.value = try {
                val result =
                    encryptedDataRepository.getAllEncryptedDataByVaultId(vaultId = vaultId)
                if (result == null) {
                    ScreenState.Error("Unable to load Passwords list")
                } else if (result.isEmpty()) {
                    ScreenState.Loaded(emptyList())
                } else {
                    //get masterCredentials
                    val masterCredentialsJson =
                        masterCredentialsRepository.getLocallyStoredMasterCredentialsJson()
                    if (masterCredentialsJson == null) {
                        ScreenState.Error(message = "Something went wrong. Login again!")
                    } else {
                        val masterCredentials: MasterCredentials =
                            masterCredentialsJson.fromJsonString()
                        val gson = Gson()

                        //decrypt
                        val decryptedList: List<PasswordDetailResult> =
                            withContext(Dispatchers.Default) {
                                result.map { encryptedData ->
                                    val decryptedData = EncryptionHelper.performDecryption(
                                        masterKey = masterCredentials.masterKey,
                                        cipherEncodedBundle = CipherEncodedBundle(
                                            encodedSalt = masterCredentials.encodedSalt,
                                            encodedInitialisationVector = encryptedData.encodedInitialisationVector,
                                            encodedEncryptedText = encryptedData.encodedEncryptedPasswordData
                                        )
                                    )
                                    val vault: Vault? =
                                        vaultRepository.getVaultById(vaultId = encryptedData.vaultId)
                                    PasswordDetailResult(
                                        passwordId = encryptedData.passwordId!!,
                                        passwordDetails = gson.fromJson(
                                            decryptedData,
                                            PasswordDetails::class.java
                                        ),
                                        vault = vault ?: Vault.defaultVault(),
                                        createdAt = encryptedData.createdAt!!,
                                        modifiedAt = encryptedData.updatedAt,
                                    )
                                }
                            }
                        ScreenState.Loaded(decryptedList)
                    }
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
                val resultVault = Vault(
                    vaultId = this@NavMenusViewModel.vaultId,
                    vaultName = this@NavMenusViewModel.vaultName,
                    iconKey = this@NavMenusViewModel.iconSelected?.name
                        ?: VaultIconsList.getIconsList()[0].name
                )

                vaultRepository.upsertVault(vault = resultVault)
                _addDialogScreenState.value = ScreenState.Loaded(resultVault)
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
}