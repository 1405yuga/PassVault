package com.example.passvault.ui.screens.authentication.create_masterkey

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CreateMasterKeyViewModel : ViewModel() {
    var uiState by mutableStateOf((MasterKeyCreationUiState()))
        private set


    fun onMasterKeyChange(masterKey: String) {
        uiState = uiState.copy(masterKey = masterKey)
    }

    fun toggleMasterKeyVisibility() {
        uiState = uiState.copy(showMasterKeyPassword = !uiState.showMasterKeyPassword)
    }

    fun setMasterKeyError(errorMsg: String) {
        uiState = uiState.copy(masterKeyError = errorMsg)
    }

    fun onConfirmedMasterKeyChange(confirmedMasterKey: String) {
        uiState = uiState.copy(confirmedMasterKey = confirmedMasterKey)
    }

    fun toggleConfirmedMasterKeyVisibility() {
        uiState =
            uiState.copy(showConfirmedMasterKeyPassword = !uiState.showConfirmedMasterKeyPassword)
    }

    fun setConfirmedMasterKeyError(errorMsg: String) {
        uiState = uiState.copy(confirmedMasterKeyError = errorMsg)
    }

    fun insertMasterKey() {
        // TODO:  add validations - uiState.password
        // TODO: insert materkey in user table
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