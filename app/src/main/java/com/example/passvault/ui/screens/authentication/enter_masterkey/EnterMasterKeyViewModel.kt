package com.example.passvault.ui.screens.authentication.enter_masterkey

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.data.CipherEncodedBundle
import com.example.passvault.data.MasterCredentials
import com.example.passvault.data.User
import com.example.passvault.di.shared_reference.MasterCredentialsRepository
import com.example.passvault.network.supabase.UserRepository
import com.example.passvault.utils.helper.EncryptionHelper
import com.example.passvault.utils.input_validations.AuthInputValidators
import com.example.passvault.utils.state.ScreenState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterMasterKeyViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val masterCredentialsRepository: MasterCredentialsRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState<String>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<String>> = _screenState

    var masterKey by mutableStateOf("")
        private set
    var masterKeyError by mutableStateOf("")
        private set
    var showMasterKeyPassword by mutableStateOf(false)
        private set

    fun onMasterKeyChange(masterKey: String) {
        this.masterKey = masterKey
    }

    fun toggleMasterKeyVisibility() {
        this.showMasterKeyPassword = !this.showMasterKeyPassword
    }

    private fun inputValidators(): Boolean {
        masterKeyError = AuthInputValidators.validatePassword(masterKey) ?: ""
        return masterKeyError.isBlank()
    }

    //steps-----
    //get userid
    //get user
    //get masterkeymaterial,
    //decrypt
    //check
    fun submitMasterKey() {
        if (!inputValidators()) return
        _screenState.value = ScreenState.Loading()

        viewModelScope.launch {
            try {
                val user = userRepository.getUser()

                if (user != null) {
                    val cipherEncodedBundle = CipherEncodedBundle(
                        encodedSalt = user.salt,
                        encodedInitialisationVector = user.initialisationVector,
                        encodedEncryptedText = user.encryptedTestText
                    )

                    try {
                        val decryptedText = EncryptionHelper.performDecryption(
                            masterKey = masterKey,
                            cipherEncodedBundle = cipherEncodedBundle
                        )

                        if (decryptedText == User.TEST_TEXT) {
                            _screenState.value =
                                ScreenState.Loaded("Master key verified successfully!")
                            val masterCredentials = Gson().toJson(
                                MasterCredentials(
                                    masterKey = masterKey,
                                    encodedSalt = user.salt
                                )
                            )
                            masterCredentialsRepository.saveMasterCredentialsJsonLocally(
                                masterCredentionsJson = masterCredentials
                            )
                        } else {
                            setIncorrectKeyError()
                        }

                    } catch (e: Exception) {
                        // Decryption error — invalid master key or other issue
                        e.printStackTrace()
                        setIncorrectKeyError()
                    }

                } else {
                    _screenState.value = ScreenState.Error("User not found")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _screenState.value = ScreenState.Error("Unable to verify!")
            }
        }
    }

    private fun setIncorrectKeyError() {
        this.masterKeyError = "Incorrect Master Key"
        _screenState.value = ScreenState.PreLoad()
    }
}