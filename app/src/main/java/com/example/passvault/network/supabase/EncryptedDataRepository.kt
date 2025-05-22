package com.example.passvault.network.supabase

import com.example.passvault.data.EncryptedData
import com.example.passvault.data.EncryptedDataTable
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedDataRepository @Inject constructor(private val supabaseClient: SupabaseClient) {

    suspend fun insertEncryptedData(encryptedData: EncryptedData) {
        supabaseClient.postgrest[EncryptedDataTable.TABLE_NAME].insert(encryptedData)
    }
}