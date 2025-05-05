package com.example.passvault.ui.screens.main.add_vault

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddVaultViewModel : ViewModel() {
    var vaultName by mutableStateOf("")
        private set

    fun onVaultNameChange(vaultName: String) {
        this.vaultName = vaultName
    }

    var vaultNameError by mutableStateOf("")
        private set

    private fun checkInputFields(): Boolean {
        vaultNameError = if (vaultName.trim().isBlank()) "Vault Name is required" else ""
        return vaultNameError.isBlank()
    }
}