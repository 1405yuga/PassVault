package com.example.passvault.network.supabase

import android.util.Log
import com.example.passvault.data.EncryptedData
import com.example.passvault.data.EncryptedDataTable
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptedDataRepository @Inject constructor(private val supabaseClient: SupabaseClient) {

    suspend fun insertEncryptedData(encryptedData: EncryptedData) {
        supabaseClient.postgrest[EncryptedDataTable.TABLE_NAME].insert(encryptedData)
    }

    suspend fun getAllEncryptedDataAtVaultId(vaultId: Long): List<EncryptedData>? {
        Log.d(this.javaClass.simpleName, "Vault id recieved : $vaultId")
        return try {
            supabaseClient.from(EncryptedDataTable.TABLE_NAME)
                .select {
                    filter {
                        eq("vault_id", vaultId)
                    }
                }
                .decodeList<EncryptedData>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
