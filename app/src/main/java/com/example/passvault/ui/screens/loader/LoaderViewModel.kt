package com.example.passvault.ui.screens.loader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.network.supabase.AuthRepository
import com.example.passvault.network.supabase.UserRepository
import com.example.passvault.ui.state.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoaderViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) :
    ViewModel() {
    private var _screenState = MutableStateFlow<ScreenState<UserState>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<UserState>> = _screenState

    init {
        observeAuthEvents()
    }

    fun observeAuthEvents() {
        viewModelScope.launch {
            authRepository.sessionStatus.collect { status ->
                _screenState.value = when (status) {
                    is SessionStatus.Authenticated -> {
                        status.session.user?.id?.let { userId ->
                            val user = userRepository.getUserById(userId)
                            ScreenState.Loaded(
                                if (user == null) UserState.DONT_HAVE_MASTER_KEY else UserState.HAVE_MASTER_KEY
                            )
                        } ?: ScreenState.Loaded(UserState.NOT_LOGGED_IN)
                    }

                    is SessionStatus.Initializing -> {
                        ScreenState.Loading()
                    }

                    is SessionStatus.NotAuthenticated -> {
                        ScreenState.Loaded(UserState.NOT_LOGGED_IN)
                    }

                    is SessionStatus.RefreshFailure -> {
                        ScreenState.Error("Unable to load!")
                    }
                }
            }
        }
    }
}

enum class UserState {
    HAVE_MASTER_KEY,
    DONT_HAVE_MASTER_KEY,
    NOT_LOGGED_IN
}