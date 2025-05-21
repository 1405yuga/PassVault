package com.example.passvault.utils.helper

import com.example.passvault.data.CipherEncodedBundle
import com.example.passvault.utils.extension_functions.fromBase64
import com.example.passvault.utils.extension_functions.toBase64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object EncryptionHelper {

    fun generateSalt(): ByteArray {
        val salt = ByteArray(size = 16)
        SecureRandom().nextBytes(salt)
        return salt
    }

    private fun deriveAESKey(masterKey: String, salt: ByteArray): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(masterKey.toCharArray(), salt, 65536, 256)
        val secret = factory.generateSecret(spec)
        return SecretKeySpec(secret.encoded, "AES")
    }

    private fun generateIV(): ByteArray {
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        return iv
    }

    private fun encrypt(plainText: String, key: SecretKeySpec, iv: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
        return cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
    }

    private fun decrypt(cipherText: ByteArray, key: SecretKeySpec, iv: ByteArray): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        val plainBytes = cipher.doFinal(cipherText)
        return String(plainBytes, Charsets.UTF_8)
    }

    fun performEncryption(plainText: String, masterKey: String, salt: ByteArray)
            : CipherEncodedBundle {
        val aesKey = deriveAESKey(
            masterKey = masterKey,
            salt = salt
        )
        val iv = generateIV()
        val encryptedData = encrypt(
            plainText = plainText,
            key = aesKey,
            iv = iv
        )
        return CipherEncodedBundle(
            encodedSalt = salt.toBase64(),
            encodedInitialisationVector = iv.toBase64(),
            encodedEncryptedTestText = encryptedData.toBase64()
        )
    }

    fun performDecryption(masterKey: String, cipherEncodedBundle: CipherEncodedBundle): String {
        val salt = cipherEncodedBundle.encodedSalt.fromBase64()
        val iv = cipherEncodedBundle.encodedInitialisationVector.fromBase64()
        val encryptedData = cipherEncodedBundle.encodedEncryptedTestText.fromBase64()

        val aesKey = deriveAESKey(
            masterKey = masterKey, salt = salt
        )

        val decryptedJson = decrypt(
            cipherText = encryptedData,
            key = aesKey,
            iv = iv
        )
        return decryptedJson
    }

}