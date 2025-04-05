package com.example.passvault

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient

@HiltAndroidApp
class PassVaultApplication : Application() {
//    lateinit var supabaseClient: SupabaseClient

    override fun onCreate() {
        super.onCreate()

//        supabaseClient = createSupabaseClient(
//            supabaseUrl = BuildConfig.SUPABASE_URL,
//            supabaseKey = BuildConfig.SUPABASE_KEY
//        ) {
//            install(Auth)
//        }
    }
}