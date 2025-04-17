package com.example.passvault.network.supabase

import com.example.passvault.data.User
import com.example.passvault.data.UserTable
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val supabaseClient: SupabaseClient) {

    val currentUserId = supabaseClient.auth.currentUserOrNull()?.id

    suspend fun insertUser(user: User) {
        supabaseClient.postgrest[UserTable.TABLE_NAME].insert(user)
    }

    suspend fun getUserById(userId: String): User? {
        return supabaseClient.postgrest[UserTable.TABLE_NAME]
            .select {
                filter { eq(UserTable.USER_ID, userId) }
            }
            .decodeSingleOrNull<User>()
    }
}