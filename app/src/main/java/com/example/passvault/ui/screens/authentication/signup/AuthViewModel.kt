package com.example.passvault.ui.screens.authentication.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.ui.state.ScreenState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.SignOutScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val supabase: SupabaseClient) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState<String>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<String>> = _screenState

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo

    private fun checkUserSessions() {
        viewModelScope.launch {
            _userInfo.value = try {
                supabase.auth.retrieveUserForCurrentSession(updateSession = true)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    init {
        checkUserSessions()
    }

    fun signUpWithEmail(email: String, password: String) {
        _screenState.value = ScreenState.Loading()
        viewModelScope.launch {
            _screenState.value = try {
                supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
                checkUserSessions()
                ScreenState.Loaded("Account created! Check your email and verify it.")
            } catch (e: Exception) {
                e.printStackTrace()
                ScreenState.Error("")
            }
        }
    }

    fun setScreenStateToPreLoad() {
        _screenState.value = ScreenState.PreLoad()
    }

    fun logoutAllSessions() {
        viewModelScope.launch {
            try {
                supabase.auth.signOut(SignOutScope.GLOBAL)
                _userInfo.value = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    fun loginWithEmail(email: String, password: String) {
//        _screenState.value = ScreenState.Loading()
//        viewModelScope.launch {
//            _screenState.value = try {
//                supabase.auth.signInWith(Email) {
//                    this.email = email
//                    this.password = password
//                }
//                checkUserSessions()
//                ScreenState.Loaded("Login successfully")
//            } catch (e: Exception) {
//                e.printStackTrace()
//                ScreenState.Error()
//            }
//        }
//    }

//    companion object {
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val application = (this[APPLICATION_KEY] as PassVaultApplication)
//                AuthViewModel(supabase = application.supabaseClient)
//            }
//        }
//    }
}