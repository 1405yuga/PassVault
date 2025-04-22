package com.example.passvault.ui.screens.authentication.signup

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
class SignUpViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _screenState = MutableStateFlow<ScreenState<String>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<String>> = _screenState

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var showPassword by mutableStateOf(false)
        private set

    var confirmPassword by mutableStateOf("")
        private set

    var showConfirmPassword by mutableStateOf(false)
        private set

    var emailError by mutableStateOf("")
        private set

    var passwordError by mutableStateOf("")
        private set

    var confirmPasswordError by mutableStateOf("")
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onConfirmPasswordChange(newPassword: String) {
        confirmPassword = newPassword
    }

    fun togglePasswordVisibility() {
        showPassword = !showPassword
    }

    fun toggleConfirmPasswordVisibility() {
        showConfirmPassword = !showConfirmPassword
    }

    private fun inputValidators(): Boolean {
        emailError = AuthInputValidators.validateEmail(email = email) ?: ""
        passwordError = AuthInputValidators.validatePassword(password = password) ?: ""
        confirmPasswordError = AuthInputValidators.validateConfirmPassword(
            password = password,
            confirmPassword = confirmPassword
        ) ?: ""
        return emailError.isBlank() and passwordError.isBlank() and confirmPasswordError.isBlank()
    }

    fun emailSignUp(email: String, password: String) {
        if (!inputValidators()) return
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