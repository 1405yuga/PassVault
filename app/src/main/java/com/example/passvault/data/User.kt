package com.example.passvault.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName(value = UserTable.USER_ID) val userId: String? = null,
    val salt: String,
    @SerialName(value = UserTable.IV) val initialisationVector: String,
    @SerialName(value = UserTable.ENCRYPTED_TEST_TEXT) val encryptedTestText: String,
    @SerialName(value = UserTable.CREATED_AT) val createdAt: String
) {
    companion object {
        const val TEST_TEXT = "Some Random text"
    }
}

data class CipherEncodedBundle(
    val encodedSalt: String,
    val encodedInitialisationVector: String,
    val encodedEncryptedTestText: String,
)

object UserTable {
    const val TABLE_NAME = "user"
    const val USER_ID = "user_id"
    const val IV = "initialisation_vector"
    const val ENCRYPTED_TEST_TEXT = "encrypted_test_text"
    const val CREATED_AT = "created_at"
}