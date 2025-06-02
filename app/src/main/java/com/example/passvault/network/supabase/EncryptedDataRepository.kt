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

    suspend fun storeEncryptedData(encryptedData: EncryptedData) {
        supabaseClient.postgrest[EncryptedDataTable.TABLE_NAME].upsert(encryptedData)
    }

    suspend fun getAllEncryptedDataByVaultId(vaultId: Long): List<EncryptedData>? {
        Log.d(this.javaClass.simpleName, "Vault id recieved : $vaultId")
        return try {
            supabaseClient.from(EncryptedDataTable.TABLE_NAME).select {
                filter {
                    eq(EncryptedDataTable.VAULT_ID, vaultId)
                }
            }.decodeList<EncryptedData>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getEncryptedDataById(id: Long): EncryptedData? {
        return try {
            supabaseClient.from(EncryptedDataTable.TABLE_NAME).select {
                filter {
                    eq(EncryptedDataTable.PASSWORD_ID, id)
                }
            }.decodeSingleOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
