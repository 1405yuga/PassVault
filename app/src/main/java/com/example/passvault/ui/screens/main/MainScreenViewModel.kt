package com.example.passvault.ui.screens.main

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.example.passvault.ui.screens.main.nav_drawer.NavDrawerMenus
import com.example.passvault.utils.extension_functions.fromJsonString
import com.example.passvault.utils.helper.EncryptionHelper
import com.example.passvault.utils.state.ScreenState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val vaultRepository: VaultRepository,
    private val encryptedDataRepository: EncryptedDataRepository,
    private val masterCredentialsRepository: MasterCredentialsRepository
) :
    ViewModel() {

    private var _vaultList = MutableStateFlow<List<Vault>>(emptyList())
    var vaultList: StateFlow<List<Vault>> = _vaultList

    var currentSelectedMenu by mutableStateOf<NavDrawerMenus?>(null)
        private set

    var lastVaultMenu by mutableStateOf<NavDrawerMenus?>(currentSelectedMenu)
        private set

    fun onMenuSelected(navDrawerMenus: NavDrawerMenus) {
        if (navDrawerMenus is NavDrawerMenus.VaultItem) {
            Log.d(this.javaClass.simpleName, "Vault menu : $navDrawerMenus")
            lastVaultMenu = navDrawerMenus
        }
        currentSelectedMenu = navDrawerMenus
    }

    fun addVaultToList(vault: Vault) {
        val updatedList = _vaultList.value.toMutableList()
        val index = updatedList.indexOfFirst { vault.vaultId == it.vaultId }
        if (index != -1) {
            updatedList[index] = vault
        } else {
            updatedList.add(vault)
        }
        _vaultList.value = updatedList
        onMenuSelected(NavDrawerMenus.VaultItem(vault))
    }

    fun removeVaultFromListById(vaultId: Long?) {
        vaultId?.let { id ->
            _vaultList.value = _vaultList.value.filter { id != it.vaultId }
        }
        onMenuSelected(NavDrawerMenus.VaultItem(_vaultList.value.last()))
    }

    private val _passwordsList = MutableStateFlow<List<PasswordDetailResult>>(emptyList())
    val passwordList: StateFlow<List<PasswordDetailResult>> = _passwordsList

    private val _vaultsAndPasswordsState =
        MutableStateFlow<ScreenState<VaultAndPasswordListResult>>(ScreenState.PreLoad())
    val vaultsAndPasswordsState: StateFlow<ScreenState<VaultAndPasswordListResult>> =
        _vaultsAndPasswordsState

    private fun loadVaultsAndPasswords() {
        _vaultsAndPasswordsState.value = ScreenState.Loading()
        viewModelScope.launch {
            try {
                val vaultsDeferred = async {
                    val result = mutableListOf<Vault>(
                        Vault(
                            vaultId = null,
                            vaultName = "All Passwords",
                            iconKey = Icons.Default.Language.name
                        )
                    )
                    result += vaultRepository.getAllVaults()
                    result
                }

                val passwordsDeferred = async {
                    val encryptedDataList = encryptedDataRepository.getAllEncryptedData()
                        ?: return@async emptyList()

                    if (encryptedDataList.isEmpty()) return@async emptyList()

                    val masterJson =
                        masterCredentialsRepository.getLocallyStoredMasterCredentialsJson()
                            ?: return@async emptyList()

                    val masterCredentials: MasterCredentials = masterJson.fromJsonString()
                    val gson = Gson()

                    withContext(Dispatchers.Default) {
                        encryptedDataList.map { encryptedData ->
                            val decryptedData = EncryptionHelper.performDecryption(
                                masterKey = masterCredentials.masterKey,
                                cipherEncodedBundle = CipherEncodedBundle(
                                    encodedSalt = masterCredentials.encodedSalt,
                                    encodedInitialisationVector = encryptedData.encodedInitialisationVector,
                                    encodedEncryptedText = encryptedData.encodedEncryptedPasswordData
                                )
                            )

                            val vault = vaultRepository.getVaultById(encryptedData.vaultId)

                            PasswordDetailResult(
                                passwordId = encryptedData.passwordId!!,
                                passwordDetails = gson.fromJson(
                                    decryptedData,
                                    PasswordDetails::class.java
                                ),
                                vault = vault ?: Vault.defaultVault(),
                                createdAt = encryptedData.createdAt!!,
                                modifiedAt = encryptedData.updatedAt
                            )
                        }
                    }
                }

                val vaults = vaultsDeferred.await()
                val passwords = passwordsDeferred.await()

                // update individual LiveData/StateFlow if needed
                _vaultList.value = vaults
                _passwordsList.value = passwords

                // select vault if needed
                onMenuSelected(NavDrawerMenus.VaultItem(vault = vaults.first()))
                _vaultsAndPasswordsState.value =
                    ScreenState.Loaded(result = VaultAndPasswordListResult(vaults, passwords))
            } catch (e: Exception) {
                e.printStackTrace()
                _vaultsAndPasswordsState.value = ScreenState.Error("Unable to load")
            }
        }
    }

    fun upsertPasswordToList(passwordDetailResult: PasswordDetailResult) {
        val updatedList = _passwordsList.value.toMutableList()
        val index = updatedList.indexOfFirst { passwordDetailResult.passwordId == it.passwordId }
        if (index != -1) {
            updatedList[index] = passwordDetailResult
        } else {
            updatedList.add(passwordDetailResult)
        }
        _passwordsList.value = updatedList
    }

    init {
        loadVaultsAndPasswords()
    }
}

data class VaultAndPasswordListResult(
    private val vaultList: List<Vault> = emptyList(),
    private val passwordDetailResultList: List<PasswordDetailResult> = emptyList()
)

sealed class MainScreens(val route: String) {
    object VaultHome : MainScreens(route = "VaultHome")
    object AddPassword : MainScreens(route = "AddPassword/{passwordDataWithId}") {
        const val ARGUMENT = "passwordDataWithId"
        fun createRoute(passwordDetailsResultString: String) =
            "AddPassword/$passwordDetailsResultString"
    }

    object Profile : MainScreens(route = "Profile")
    object ViewPassword : MainScreens(route = "ViewPassword/{passwordDataWithId}") {
        const val ARGUMENT = "passwordDataWithId"
        fun createRoute(passwordDetailsResultString: String) =
            "ViewPassword/$passwordDetailsResultString"
    }
}