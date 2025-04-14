package com.example.passvault.ui.screens.authentication.enter_masterkey

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.passvault.utils.AuthInputValidators

class MasterKeyViewModel : ViewModel() {
    var uiState by mutableStateOf((MasterKeyUiState()))
        private set

    fun onMasterKeyChange(masterKey: String) {
        uiState = uiState.copy(masterKey = masterKey)
    }

    fun toggleMasterKeyVisibility() {
        uiState = uiState.copy(showMasterKeyPassword = !uiState.showMasterKeyPassword)
    }

    private fun inputValidators() {
        uiState = uiState.copy(
            masterKeyError = AuthInputValidators.validatePassword(uiState.masterKey) ?: ""
        )
    }

    fun submitMasterKey() {
        //   add validations - uiState.password
        inputValidators()
        // TODO: check materkey
    }
}

data class MasterKeyUiState(
    val masterKey: String = "",
    val masterKeyError: String = "",
    val showMasterKeyPassword: Boolean = false,
)