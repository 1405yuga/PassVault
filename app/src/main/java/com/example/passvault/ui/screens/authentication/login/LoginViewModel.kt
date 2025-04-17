package com.example.passvault.ui.screens.authentication.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.network.supabase.AuthRepository
import com.example.passvault.ui.state.ScreenState
import com.example.passvault.utils.AuthInputValidators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState<String>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<String>> = _screenState

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(newEmail: String) {
        uiState = uiState.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        uiState = uiState.copy(password = newPassword)
    }

    fun togglePasswordVisibility() {
        uiState = uiState.copy(showPassword = !uiState.showPassword)
    }

    private fun inputValidators(): Boolean {
        uiState = uiState.copy(
            emailError = AuthInputValidators.validateEmail(email = uiState.email) ?: "",
            passwordError = AuthInputValidators.validatePassword(password = uiState.password) ?: ""
        )
        return uiState.emailError.isBlank() and uiState.passwordError.isBlank()
    }

    fun emailLogin() {
        if (!inputValidators()) return
        _screenState.value = ScreenState.Loading()
        viewModelScope.launch {
            _screenState.value = try {
                authRepository.emailLogin(
                    email = uiState.email.trim(),
                    password = uiState.password.trim()
                )
                Log.d("Login", "LOgged in!!!")
                ScreenState.Loaded("Login successfully")
            } catch (e: Exception) {
                e.printStackTrace()
                ScreenState.Error("Unable to Login. Something went wrong!")
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val emailError: String = "",
    val password: String = "",
    val passwordError: String = "",
    val showPassword: Boolean = false,
)