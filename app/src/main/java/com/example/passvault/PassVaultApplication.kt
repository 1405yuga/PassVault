package com.example.passvault

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

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