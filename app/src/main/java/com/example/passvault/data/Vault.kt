package com.example.passvault.data

import androidx.compose.ui.graphics.vector.ImageVector

data class Vault(
    val vaultId: String,
    val userId: String,
    val vaultName: String,
    val imageVector: ImageVector
)
