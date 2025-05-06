package com.example.passvault.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Vault(
    @SerialName(VaultTable.VAULT_ID) val vaultId: Long? = null,
    @SerialName(VaultTable.USER_ID) val userId: String? = null,
    @SerialName(VaultTable.VAULT_NAME) val vaultName: String,
    @SerialName(VaultTable.ICON_KEY) val iconKey: String,
    @SerialName(VaultTable.CREATED_AT) val createdAt: String? = null
) {
    companion object {
        fun defaultVault(): Vault = Vault(
            vaultId = 0L,
            userId = "",
            vaultName = "Personal",
            iconKey = Icons.Outlined.Home.name,
            createdAt = ""
        )
    }
}

object VaultTable {
    const val TABLE_NAME = "vault"
    const val VAULT_ID = "vault_id"
    const val USER_ID = "user_id"
    const val VAULT_NAME = "vault_name"
    const val ICON_KEY = "icon_key"
    const val CREATED_AT = "created_at"
}
