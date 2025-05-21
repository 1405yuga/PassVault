package com.example.passvault.di.shared_reference

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EncryptedPrefsModule {
    private const val fileName = "secure_prefs"

    val mockSharedPreference = object : SharedPreferences {
        override fun getAll(): Map<String?, String>? = emptyMap()
        override fun getString(p0: String?, p1: String?): String? = null
        override fun getStringSet(
            p0: String?,
            p1: Set<String?>?
        ): Set<String?>? = null

        override fun getInt(p0: String?, p1: Int): Int = 0
        override fun getLong(p0: String?, p1: Long): Long = 0
        override fun getFloat(p0: String?, p1: Float): Float = 0f
        override fun getBoolean(p0: String?, p1: Boolean): Boolean = false
        override fun contains(p0: String?): Boolean = false
        override fun edit(): SharedPreferences.Editor? = null
        override fun registerOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {}
        override fun unregisterOnSharedPreferenceChangeListener(p0: SharedPreferences.OnSharedPreferenceChangeListener?) {}
    }

    @Provides
    @Singleton
    fun provideEncryptedSharedReference(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            fileName,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}