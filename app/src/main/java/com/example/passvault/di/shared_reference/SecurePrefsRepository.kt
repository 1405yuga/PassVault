package com.example.passvault.di.shared_reference

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class SecurePrefsRepository @Inject constructor(private val encryptedPrefs: SharedPreferences) {
    private companion object {
        val SECURE_KEY = "KEY"
    }

    fun saveMasterKeyLocally(masterKey: String?) {
        encryptedPrefs.edit { putString(SECURE_KEY, masterKey) }
    }

    fun getLocallyStoredMasterKey(): String? {
        return encryptedPrefs.getString(SECURE_KEY, null)
    }
}