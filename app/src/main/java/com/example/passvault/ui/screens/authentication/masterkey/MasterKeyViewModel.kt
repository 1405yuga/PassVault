package com.example.passvault.ui.screens.authentication.masterkey

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MasterKeyViewModel : ViewModel() {
    var uiState by mutableStateOf((MasterKeyUiState()))
        private set

    fun onPasswordChange(newPassword: String) {
        uiState = uiState.copy(password = newPassword)
    }

    fun onPasswordError(error: String) {
        uiState = uiState.copy(passwordError = error)
    }

    fun togglePasswordVisibility() {
        uiState = uiState.copy(showPassword = !uiState.showPassword)
    }
}

data class MasterKeyUiState(
    val password: String = "",
    val passwordError: String = "",
    val showPassword: Boolean = false
)