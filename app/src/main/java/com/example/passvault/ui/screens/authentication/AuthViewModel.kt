package com.example.passvault.ui.screens.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.passvault.PassVaultApplication
import com.example.passvault.ui.screens.state.ScreenState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val supabase: SupabaseClient) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState<String>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<String>> = _screenState

    fun signUpWithEmail(email: String, password: String) {
        _screenState.value = ScreenState.Loading()
        viewModelScope.launch {
            _screenState.value = try {
                supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
                ScreenState.Loaded("SignUp successfully!")
            } catch (e: Exception) {
                e.printStackTrace()
                ScreenState.Error()
            }
        }
    }

    fun setScreenStateToPreLoad() {
        _screenState.value = ScreenState.PreLoad()
    }

    fun loginWithEmail(email: String, password: String) {
        _screenState.value = ScreenState.Loading()
        viewModelScope.launch {
            _screenState.value = try {
                supabase.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                ScreenState.Loaded("Login successfull")
            } catch (e: Exception) {
                e.printStackTrace()
                ScreenState.Error()
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PassVaultApplication)
                AuthViewModel(supabase = application.supabaseClient)
            }
        }
    }
}