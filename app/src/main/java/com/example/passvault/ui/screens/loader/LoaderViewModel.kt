package com.example.passvault.ui.screens.loader

import android.util.Log
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
class LoaderViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private var _screenState = MutableStateFlow<ScreenState<UserState>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<UserState>> = _screenState

    fun checkSession() {
        _screenState.value = ScreenState.Loading()
        viewModelScope.launch {
            try {
                val loggedIn = authRepository.checkIfUserLoggedIn()
                Log.d("LoaderViewModel", "Logged in : $loggedIn")
                _screenState.value = if (loggedIn) {
                    // TODO: check if have masterkey
                    ScreenState.Loaded(UserState.DONT_HAVE_MASTER_KEY)
                } else {
                    ScreenState.Loaded(UserState.NOT_LOGGED_IN)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _screenState.value = ScreenState.Error("Unable to load")
            }
        }
    }
}

enum class UserState {
    HAVE_MASTER_KEY,
    DONT_HAVE_MASTER_KEY,
    NOT_LOGGED_IN
}