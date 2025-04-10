package com.example.passvault.ui.screens.authentication.signup

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
class SignUpViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState<String>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<String>> = _screenState

    var uiState by mutableStateOf(SignUpUiState())
        private set

    fun onEmailChange(newEmail: String) {
        uiState = uiState.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        uiState = uiState.copy(password = newPassword)
    }

    fun onConfirmPasswordChange(newPassword: String) {
        uiState = uiState.copy(confirmPassword = newPassword)
    }

    fun togglePasswordVisibility() {
        uiState = uiState.copy(showPassword = !uiState.showPassword)
    }

    fun toggleConfirmPasswordVisibility() {
        uiState = uiState.copy(showConfirmPassword = !uiState.showConfirmPassword)
    }

    fun setEmailError(errorMsg: String) {
        uiState = uiState.copy(emailError = errorMsg)
    }

    fun setPasswordError(errorMsg: String) {
        uiState = uiState.copy(passwordError = errorMsg)
    }

    fun setConfirmPasswordError(errorMsg: String) {
        uiState = uiState.copy(confirmPasswordError = errorMsg)
    }

    fun emailSignUp(email: String, password: String) {
        if (uiState.emailError.isBlank() and uiState.passwordError.isBlank() and uiState.confirmPasswordError.isBlank()) {
            _screenState.value = ScreenState.Loading()
            viewModelScope.launch {
                _screenState.value = try {
                    authRepository.emailSignUp(email = email, password = password)
                    ScreenState.Loaded("Account created! Check your email and verify it.")
                } catch (e: Exception) {
                    e.printStackTrace()
                    ScreenState.Error("Unable to SignUp. Something went wrong!")
                }
            }
        }
    }
}

data class SignUpUiState(
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val confirmPassword: String = "",
    val showConfirmPassword: Boolean = false,
    val emailError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = "",
)