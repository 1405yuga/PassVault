package com.example.passvault.ui.screens.authentication.create_masterkey

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.data.User
import com.example.passvault.network.supabase.UserRepository
import com.example.passvault.utils.encryption.EncryptionHelper
import com.example.passvault.utils.input_validations.AuthInputValidators
import com.example.passvault.utils.state.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class CreateMasterKeyViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState<String>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<String>> = _screenState
    var masterKey by mutableStateOf("")
        private set

    var masterKeyError by mutableStateOf("")
        private set

    var showMasterKeyPassword by mutableStateOf(false)
        private set
    var confirmedMasterKey by mutableStateOf("")
        private set

    var confirmedMasterKeyError by mutableStateOf("")
        private set

    var showConfirmedMasterKeyPassword by mutableStateOf(false)
        private set

    fun onMasterKeyChange(masterKey: String) {
        this.masterKey = masterKey
    }

    fun toggleMasterKeyVisibility() {
        this.showMasterKeyPassword = !this.showMasterKeyPassword
    }

    fun onConfirmedMasterKeyChange(confirmedMasterKey: String) {
        this.confirmedMasterKey = confirmedMasterKey
    }

    fun toggleConfirmedMasterKeyVisibility() {
        this.showConfirmedMasterKeyPassword = !this.showConfirmedMasterKeyPassword
    }

    private fun inputValidators(): Boolean {
        masterKeyError = AuthInputValidators.validatePassword(password = masterKey) ?: ""
        confirmedMasterKeyError = AuthInputValidators.validateConfirmPassword(
            password = masterKey,
            confirmPassword = confirmedMasterKey
        ) ?: ""
        return masterKeyError.isBlank() and confirmedMasterKeyError.isBlank()
    }

    fun insertMasterKey() {
        if (!inputValidators()) return
        _screenState.value = ScreenState.Loading()
        viewModelScope.launch {
            try {
                val masterKeyMaterial = EncryptionHelper.performEncryption(
                    plainText = User.TEST_TEXT,
                    masterKey = masterKey
                )
                userRepository.insertUser(
                    user = User(
                        salt = masterKeyMaterial.encodedSalt,
                        initialisationVector = masterKeyMaterial.encodedInitialisationVector,
                        encryptedTestText = masterKeyMaterial.encodedEncryptedTestText,
                        createdAt = Clock.System.now().toString()
                    )
                )
                _screenState.value =
                    ScreenState.Loaded("Added master key successfully!")
            } catch (e: Exception) {
                e.printStackTrace()
                _screenState.value = ScreenState.Error("Unable to insert")
            }
        }
    }
}