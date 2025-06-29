package com.example.passvault.ui.screens.main.add_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.data.CipherEncodedBundle
import com.example.passvault.data.EncryptedData
import com.example.passvault.data.MasterCredentials
import com.example.passvault.data.PasswordDetailResult
import com.example.passvault.data.PasswordDetails
import com.example.passvault.data.Vault
import com.example.passvault.di.shared_reference.MasterCredentialsRepository
import com.example.passvault.network.supabase.EncryptedDataRepository
import com.example.passvault.utils.extension_functions.fromBase64
import com.example.passvault.utils.extension_functions.fromJsonString
import com.example.passvault.utils.extension_functions.toJsonString
import com.example.passvault.utils.helper.EncryptionHelper
import com.example.passvault.utils.state.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpsertPasswordDetailViewModel @Inject constructor(
    private val masterCredentialsRepository: MasterCredentialsRepository,
    private val encryptedDataRepository: EncryptedDataRepository
) :
    ViewModel() {

    var title by mutableStateOf("")
        private set
    var username by mutableStateOf("") //optional
        private set
    var password by mutableStateOf("")
        private set
    var showPassword by mutableStateOf(false)
        private set
    var website by mutableStateOf("") //optional
        private set
    var notes by mutableStateOf("") //optional
        private set
    var selectedVault by mutableStateOf(Vault.defaultVault())
        private set

    fun loadInitialData(passwordDetailResult: PasswordDetailResult?) {
        //load fields--------------------------------------
        passwordDetailResult?.passwordDetails?.let {
            title = it.title
            username = it.email
            password = it.password
            website = it.website
            notes = it.notes
        }
    }

    fun onTitleChange(title: String) {
        this.title = title
    }

    fun onUsernameChange(username: String) {
        this.username = username
    }

    fun onPasswordChange(password: String) {
        this.password = password
    }

    fun togglePasswordVisibility() {
        showPassword = !showPassword
    }

    fun onWebsiteChange(website: String) {
        this.website = website
    }

    fun onNotesChange(notes: String) {
        this.notes = notes
    }

    fun onSelectedVaultChange(vault: Vault) {
        this.selectedVault = vault
    }

    var titleError by mutableStateOf("")
        private set

    var passwordError by mutableStateOf("")
        private set

    private fun areInputsValid(): Boolean {
        titleError = if (title.trim().isBlank()) "Title is required" else ""
        passwordError = if (password.trim().isBlank()) "Password is required" else ""
        return titleError.isBlank() && passwordError.isBlank()
    }

    private val _screenState =
        MutableStateFlow<ScreenState<PasswordDetailResult>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<PasswordDetailResult>> = _screenState

    //get master-credentials
    //create passwordDetailJsonString
    // encrypt password details
    //store in db
    fun storePasswordDetails(passwordId: Long?) {
        if (!areInputsValid()) return
        _screenState.value = ScreenState.Loading()
        try {
            viewModelScope.launch {
                val masterCredentialsJson =
                    masterCredentialsRepository.getLocallyStoredMasterCredentialsJson()
                if (masterCredentialsJson == null) {
                    _screenState.value = ScreenState.Error(message = "Login again!")
                } else {
                    val masterCredentials: MasterCredentials =
                        masterCredentialsJson.fromJsonString()
                    val passwordDetails = PasswordDetails(
                        title = title.trim(),
                        email = username.trim(),
                        password = password.trim(),
                        website = website.trim(),
                        notes = notes.trim()
                    )
                    val passwordDetailJsonString: String = passwordDetails.toJsonString()
                    val cipherEncodedBundle: CipherEncodedBundle =
                        EncryptionHelper.performEncryption(
                            plainText = passwordDetailJsonString,
                            masterKey = masterCredentials.masterKey,
                            salt = masterCredentials.encodedSalt.fromBase64()
                        )

                    val encryptedData = EncryptedData(
                        passwordId = passwordId,
                        vaultId = selectedVault.vaultId,
                        encodedInitialisationVector = cipherEncodedBundle.encodedInitialisationVector,
                        encodedEncryptedPasswordData = cipherEncodedBundle.encodedEncryptedText
                    )
                    val resultEncryptedData =
                        encryptedDataRepository.storeEncryptedData(encryptedData = encryptedData)
                    _screenState.value = if (resultEncryptedData == null) {
                        ScreenState.Error("Something went wrong")
                    } else {
                        ScreenState.Loaded(
                            result = PasswordDetailResult(
                                passwordId = resultEncryptedData.passwordId!!,
                                passwordDetails = passwordDetails,
                                vault = selectedVault,
                                createdAt = resultEncryptedData.createdAt!!,
                                modifiedAt = resultEncryptedData.updatedAt
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _screenState.value = ScreenState.Error(message = "Unable to add password")
        }
    }
}