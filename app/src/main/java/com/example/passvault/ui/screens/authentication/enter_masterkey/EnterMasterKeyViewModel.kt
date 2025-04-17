package com.example.passvault.ui.screens.authentication.enter_masterkey

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.data.User
import com.example.passvault.data.UserMasterKeyMaterial
import com.example.passvault.network.supabase.AuthRepository
import com.example.passvault.network.supabase.UserRepository
import com.example.passvault.ui.state.ScreenState
import com.example.passvault.utils.AuthInputValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.example.EncryptionHelper
import javax.inject.Inject

@HiltViewModel
class EnterMasterKeyViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState<String>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<String>> = _screenState

    var uiState by mutableStateOf((MasterKeyUiState()))
        private set

    fun onMasterKeyChange(masterKey: String) {
        uiState = uiState.copy(masterKey = masterKey)
    }

    fun toggleMasterKeyVisibility() {
        uiState = uiState.copy(showMasterKeyPassword = !uiState.showMasterKeyPassword)
    }

    private fun inputValidators(): Boolean {
        uiState = uiState.copy(
            masterKeyError = AuthInputValidators.validatePassword(uiState.masterKey) ?: ""
        )
        return uiState.masterKeyError.isBlank()
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
                val status = authRepository.sessionStatus.first()

                if (status is SessionStatus.Authenticated) {
                    val userId = status.session.user?.id

                    if (userId != null) {
                        Log.d(this@EnterMasterKeyViewModel.javaClass.simpleName, "userId : $userId")
                        val user = userRepository.getUserById(userId)

                        if (user != null) {
                            val userMasterKeyMaterial = UserMasterKeyMaterial(
                                encodedSalt = user.salt,
                                encodedInitialisationVector = user.initialisationVector,
                                encodedEncryptedTestText = user.encryptedTestText
                            )

                            try {
                                val decryptedText = EncryptionHelper.performDecryption(
                                    masterKey = uiState.masterKey,
                                    userMasterKeyMaterial = userMasterKeyMaterial
                                )

                                if (decryptedText == User.TEST_TEXT) {
                                    _screenState.value =
                                        ScreenState.Loaded("Master key verified successfully!")
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

                    } else {
                        Log.d(this@EnterMasterKeyViewModel.javaClass.simpleName, "User ID is null")
                        _screenState.value = ScreenState.Error("User ID not found")
                    }
                } else {
                    Log.d(this@EnterMasterKeyViewModel.javaClass.simpleName, "NOT AUTHENTICATED")
                    _screenState.value = ScreenState.Error("Not Authenticated User")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _screenState.value = ScreenState.Error("Unable to verify!")
            }
        }
    }

    private fun setIncorrectKeyError() {
        uiState = uiState.copy(masterKeyError = "Incorrect Master Key")
        _screenState.value = ScreenState.PreLoad()
    }
}

data class MasterKeyUiState(
    val masterKey: String = "",
    val masterKeyError: String = "",
    val showMasterKeyPassword: Boolean = false,
)