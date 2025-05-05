package com.example.passvault.ui.screens.main.add_vault

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel

class AddVaultViewModel : ViewModel() {
    val iconList = listOf(
        Icons.Default.Home,
        Icons.Default.Person,
        Icons.Default.Lock,
        Icons.Default.Email,
        Icons.Default.Star,
        Icons.Default.Phone,
        Icons.Default.ShoppingCart,
        Icons.Default.Settings
    )

    var vaultName by mutableStateOf("")
        private set

    fun onVaultNameChange(vaultName: String) {
        this.vaultName = vaultName
    }

    var vaultNameError by mutableStateOf("")
        private set
    var currentSelectedIcon by mutableStateOf(iconList[0])
        private set

    fun onIconSelected(icon: ImageVector) {
        this.currentSelectedIcon = icon
    }

    private fun checkInputFields(): Boolean {
        vaultNameError = if (vaultName.trim().isBlank()) "Vault Name is required" else ""
        return vaultNameError.isBlank()
    }

    fun clearInputs() {
        vaultName = ""
        vaultNameError = ""
        currentSelectedIcon = iconList[0]
    }
}