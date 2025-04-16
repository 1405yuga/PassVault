package com.example.passvault.data

import android.util.Base64
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName(value = UserTable.USER_ID) val userId: String,
    val salt: String,
    @SerialName(value = UserTable.IV) val initialisationVector: String,
    @SerialName(value = UserTable.ENCRYPTED_TEST_TEXT) val encryptedTestText: String,
    @SerialName(value = UserTable.CREATED_AT) val createdAt: String
) {
    companion object {
        const val TEST_TEXT = "Some Random text"
    }
}

data class UserMasterKeyMaterial(
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

fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.DEFAULT)
fun String.fromBase64(): ByteArray = Base64.decode(this, Base64.DEFAULT)