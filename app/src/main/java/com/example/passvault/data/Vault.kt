package com.example.passvault.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

data class Vault(
    val vaultId: String,
    val userId: String,
    val vaultName: String,
    val imageVector: ImageVector
) {
    companion object {
        fun defaultVault(): Vault = Vault(
            vaultId = "DEFAULT",
            userId = "",
            vaultName = "Personal",
            imageVector = Icons.Outlined.Home
        )
    }
}
