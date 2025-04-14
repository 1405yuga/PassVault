package com.example.passvault.ui.screens.authentication.create_masterkey

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.passvault.utils.AuthInputValidators

class CreateMasterKeyViewModel : ViewModel() {
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
        //   add validations - uiState.password
        inputValidators()
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