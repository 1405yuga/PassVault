package com.example.passvault.ui.screens.authentication.create_masterkey

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.data.User
import com.example.passvault.network.supabase.UserRepository
import com.example.passvault.ui.state.ScreenState
import com.example.passvault.utils.AuthInputValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.example.EncryptionHelper
import javax.inject.Inject

@HiltViewModel
class CreateMasterKeyViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState<String>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<String>> = _screenState
    var uiState by mutableStateOf((MasterKeyCreationUiState()))
        private set

    fun onMasterKeyChange(masterKey: String) {
        uiState = uiState.copy(masterKey = masterKey)
    }

    fun toggleMasterKeyVisibility() {
        uiState = uiState.copy(showMasterKeyPassword = !uiState.showMasterKeyPassword)
    }

    fun onConfirmedMasterKeyChange(confirmedMasterKey: String) {
        uiState = uiState.copy(confirmedMasterKey = confirmedMasterKey)
    }

    fun toggleConfirmedMasterKeyVisibility() {
        uiState =
            uiState.copy(showConfirmedMasterKeyPassword = !uiState.showConfirmedMasterKeyPassword)
    }

    private fun inputValidators() {
        uiState = uiState.copy(
            masterKeyError = AuthInputValidators.validatePassword(
                password = uiState.masterKey
            ) ?: "",
            confirmedMasterKeyError = AuthInputValidators.validateConfirmPassword(
                password = uiState.masterKey,
                confirmPassword = uiState.confirmedMasterKey
            ) ?: ""
        )
    }

    fun insertMasterKey() {
        _screenState.value = ScreenState.Loading()
        inputValidators()
        viewModelScope.launch {
            _screenState.value = try {
                val masterKeyMaterial = EncryptionHelper.performEncryption(
                    plainText = User.TEST_TEXT,
                    masterKey = uiState.masterKey
                )
//                val userId = userRepository.currentUserId
                // TODO: get userId from sessions 
                val userId = "1245"
                if (userId == null) {
                    ScreenState.Error(message = "User not found")
                } else {
                    userRepository.insertUser(
                        user =
                            User(
                                userId = userId,
                                salt = masterKeyMaterial.encodedSalt,
                                initialisationVector = masterKeyMaterial.encodedInitialisationVector,
                                encryptedTestText = masterKeyMaterial.encodedEncryptedTestText,
                                createdAt = Clock.System.now().toString()
                            )
                    )
                    ScreenState.Loaded(result = "Added master key successfully!")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ScreenState.Error("Unable to insert")
            }
        }
    }
}

data class MasterKeyCreationUiState(
    val masterKey: String = "",
    val masterKeyError: String = "",
    val showMasterKeyPassword: Boolean = false,
    val confirmedMasterKey: String = "",
    val confirmedMasterKeyError: String = "",
    val showConfirmedMasterKeyPassword: Boolean = false,
)