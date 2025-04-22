package com.example.passvault.network.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val supabaseClient: SupabaseClient) {

//    val sessionStatus = supabaseClient.auth.sessionStatus

//    suspend fun getCurrentAuthUserInfo(): UserInfo? {
//        val status = sessionStatus.first()
//        return if (status is SessionStatus.Authenticated) status.session.user
//        else null
//    }

    fun getSessionStatus(): StateFlow<SessionStatus> {
        return supabaseClient.auth.sessionStatus
    }

    suspend fun emailLogin(email: String, password: String) {
        supabaseClient.auth.signOut()
        return supabaseClient.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun emailSignUp(email: String, password: String) =
        supabaseClient.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
}