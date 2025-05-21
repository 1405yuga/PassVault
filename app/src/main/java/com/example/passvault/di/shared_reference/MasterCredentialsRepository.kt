package com.example.passvault.di.shared_reference

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class MasterCredentialsRepository @Inject constructor(private val encryptedPrefs: SharedPreferences) {
    private companion object {
        val SECURE_KEY = "KEY"
    }

    fun saveMasterCredentialsJsonLocally(masterCredentionsJson: String?) {
        encryptedPrefs.edit { putString(SECURE_KEY, masterCredentionsJson) }
    }

    fun getLocallyStoredMasterCredentialsJson(): String? {
        return encryptedPrefs.getString(SECURE_KEY, null)
    }
}