package com.example.passvault.ui.screens.authentication.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.network.supabase.AuthRepository
import com.example.passvault.ui.state.ScreenState
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

    fun setEmailError(errorMsg: String) {
        uiState = uiState.copy(emailError = errorMsg)
    }

    fun setPasswordError(errorMsg: String) {
        uiState = uiState.copy(passwordError = errorMsg)
    }

    fun emailLogin(email: String, password: String) {
        if (uiState.emailError.isBlank() and uiState.passwordError.isBlank()) {
            _screenState.value = ScreenState.Loading()
            viewModelScope.launch {
                _screenState.value = try {
                    authRepository.emailLogin(email = email, password = password)
                    Log.d("Login", "LOgged in!!!")
                    ScreenState.Loaded("Login successfully")
                } catch (e: Exception) {
                    e.printStackTrace()
                    ScreenState.Error("Unable to Login. Something went wrong!")
                }
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