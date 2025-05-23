package com.example.passvault.ui.screens.authentication.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.network.supabase.AuthRepository
import com.example.passvault.utils.input_validations.AuthInputValidators
import com.example.passvault.utils.state.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState<String>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<String>> = _screenState

    var email by mutableStateOf("")
        private set

    var emailError by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var passwordError by mutableStateOf("")
        private set

    var showPassword by mutableStateOf(false)
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun togglePasswordVisibility() {
        showPassword = !showPassword
    }

    private fun inputValidators(): Boolean {
        emailError = AuthInputValidators.validateEmail(email = email) ?: ""
        passwordError = AuthInputValidators.validatePassword(password = password) ?: ""
        return emailError.isBlank() and passwordError.isBlank()
    }

    fun emailLogin() {
        if (!inputValidators()) return
        _screenState.value = ScreenState.Loading()
        viewModelScope.launch {
            _screenState.value = try {
                authRepository.emailLogin(
                    email = email.trim(),
                    password = password.trim()
                )
                Log.d("Login", "Logged in!!!")
                ScreenState.Loaded("Login successfully")
            } catch (e: Exception) {
                e.printStackTrace()
                ScreenState.Error("Unable to Login. Something went wrong!")
            }
        }
    }
}