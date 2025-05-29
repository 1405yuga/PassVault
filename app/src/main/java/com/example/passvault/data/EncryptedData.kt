package com.example.passvault.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EncryptedData(
    @SerialName(EncryptedDataTable.PASSWORD_ID) val passwordId: Long? = null,
    @SerialName(EncryptedDataTable.VAULT_ID) val vaultId: Long? = null, //foreign key - Vault
    @SerialName(EncryptedDataTable.USER_ID) val userId: String? = null, //foreign key - Vault
    @SerialName(EncryptedDataTable.ENCODED_IV) val encodedInitialisationVector: String, //generate
    @SerialName(EncryptedDataTable.ENCODED_ENCRYPTED_DATA) val encodedEncryptedPasswordData: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class PasswordDetails(
    val title: String,
    val email: String = "",
    val password: String,
    val website: String = "",
    val notes: String = ""
) {
    companion object {
        val mockPasswordDetails = PasswordDetails(
            title = "Google",
            email = "xyz@gmail.com",
            password = "123456",
            website = "www.google.com",
            notes = "My notes can be anything.\nsomething"
        )
    }
}

object EncryptedDataTable {
    const val TABLE_NAME = "encrypted_data"
    const val PASSWORD_ID = "password_id"
    const val VAULT_ID = "vault_id"
    const val USER_ID = "user_id"
    const val ENCODED_IV = "encoded_iv"
    const val ENCODED_ENCRYPTED_DATA = "encoded_encrypted_data"
    const val CREATED_AT = "created_at"
    const val UPDATED_AT = "updated_at"
}