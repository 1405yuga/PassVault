package com.example.passvault.ui.screens.authentication.signup

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

    fun emailSignUp(email: String, password: String) {
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