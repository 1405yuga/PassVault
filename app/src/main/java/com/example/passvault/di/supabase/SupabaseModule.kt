package com.example.passvault.di.supabase

import com.example.passvault.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SupabaseModule {

    companion object {
        val mockClient = createSupabaseClient(
            supabaseUrl = "",
            supabaseKey = "",
            builder = { install(Auth) }
        )
    }

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL, supabaseKey = BuildConfig.SUPABASE_KEY
        ) {
            install(Auth)
            install(Postgrest)
        }

    }
}
