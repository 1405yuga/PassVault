package com.example.passvault.network.supabase

import com.example.passvault.data.Vault
import com.example.passvault.data.VaultTable
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaultRepository @Inject constructor(private val supabaseClient: SupabaseClient) {

    suspend fun upsertVault(vault: Vault): Vault? {
        return supabaseClient.postgrest[VaultTable.TABLE_NAME].upsert(value = vault) {
            onConflict = VaultTable.VAULT_ID
            select()
        }.decodeSingleOrNull<Vault>()
    }

    suspend fun getAllVaults(): List<Vault> {
        return supabaseClient
            .postgrest[VaultTable.TABLE_NAME]
            .select(columns = Columns.ALL)
            .decodeList()
    }

    suspend fun getVaultById(vaultId: Long?): Vault? {
        return try {
            vaultId?.let {
                supabaseClient.postgrest[VaultTable.TABLE_NAME]
                    .select {
                        filter {
                            eq(column = VaultTable.VAULT_ID, vaultId)
                        }
                    }.decodeSingleOrNull()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deleteVault(vaultId: Long?): Result<Unit> {
        return runCatching {
            vaultId?.let {
                supabaseClient
                    .postgrest[VaultTable.TABLE_NAME]
                    .delete {
                        filter {
                            eq(
                                VaultTable.VAULT_ID,
                                vaultId
                            )
                        }
                    }
            } ?: run {
                throw NullPointerException("Vault Id is null")
            }
        }
    }
}