package com.example.passvault.network.supabase

import com.example.passvault.data.User
import com.example.passvault.data.UserTable
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val supabaseClient: SupabaseClient) {

    suspend fun insertUser(user: User) {
        supabaseClient.postgrest[UserTable.TABLE_NAME].insert(user)
    }

    suspend fun getUser(): User? {
        return supabaseClient
            .postgrest[UserTable.TABLE_NAME]
            .select(Columns.ALL)
            .decodeSingleOrNull<User>()
    }
}