package com.example.passvault.ui.screens.authentication.create_masterkey

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.data.User
import com.example.passvault.network.supabase.AuthRepository
import com.example.passvault.network.supabase.UserRepository
import com.example.passvault.ui.state.ScreenState
import com.example.passvault.utils.AuthInputValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.example.EncryptionHelper
import javax.inject.Inject

@HiltViewModel
class CreateMasterKeyViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) :
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

    private fun inputValidators(): Boolean {
        uiState = uiState.copy(
            masterKeyError = AuthInputValidators.validatePassword(
                password = uiState.masterKey
            ) ?: "",
            confirmedMasterKeyError = AuthInputValidators.validateConfirmPassword(
                password = uiState.masterKey,
                confirmPassword = uiState.confirmedMasterKey
            ) ?: ""
        )
        return uiState.masterKeyError.isBlank() and uiState.confirmedMasterKeyError.isBlank()
    }

    fun insertMasterKey() {
        if (!inputValidators()) return
        _screenState.value = ScreenState.Loading()
        viewModelScope.launch {
            try {
                val masterKeyMaterial = EncryptionHelper.performEncryption(
                    plainText = User.TEST_TEXT,
                    masterKey = uiState.masterKey
                )
                authRepository.sessionStatus.collect { status ->
                    if (status is SessionStatus.Authenticated) {
                        status.session.user?.id?.let { userId ->
                            Log.d(
                                this@CreateMasterKeyViewModel.javaClass.simpleName,
                                "userId : $userId"
                            )
                            userRepository.insertUser(
                                user = User(
                                    userId = userId,
                                    salt = masterKeyMaterial.encodedSalt,
                                    initialisationVector = masterKeyMaterial.encodedInitialisationVector,
                                    encryptedTestText = masterKeyMaterial.encodedEncryptedTestText,
                                    createdAt = Clock.System.now().toString()
                                )
                            )
                            _screenState.value =
                                ScreenState.Loaded("Added master key successfully!")
                        } ?: {
                            Log.d(
                                this@CreateMasterKeyViewModel.javaClass.simpleName,
                                "User id null"
                            )
                            _screenState.value = ScreenState.Error("User Id not found")
                        }
                    } else {
                        Log.d(
                            this@CreateMasterKeyViewModel.javaClass.simpleName,
                            "NOT AUTHENTICATED"
                        )
                        _screenState.value = ScreenState.Error("Not Authenticated User")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _screenState.value = ScreenState.Error("Unable to insert")
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