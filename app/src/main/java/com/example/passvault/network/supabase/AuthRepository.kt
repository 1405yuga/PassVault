package com.example.passvault.network.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val supabaseClient: SupabaseClient) {
    suspend fun emailLogin(email: String, password: String) =
        supabaseClient.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }

    suspend fun emailSignUp(email: String, password: String) =
        supabaseClient.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
}