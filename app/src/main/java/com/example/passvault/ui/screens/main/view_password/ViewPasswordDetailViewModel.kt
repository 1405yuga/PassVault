package com.example.passvault.ui.screens.main.view_password

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.data.PasswordDetails
import com.example.passvault.network.supabase.EncryptedDataRepository
import com.example.passvault.utils.state.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewPasswordDetailViewModel @Inject constructor(private val encryptedDataRepository: EncryptedDataRepository) :
    ViewModel() {

    private val _screenState =
        MutableStateFlow<ScreenState<PasswordDetailResult>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<PasswordDetailResult>> = _screenState

    fun loadPasswordDetails(passwordId: Long?) {
        if (passwordId == null) {
            _screenState.value = ScreenState.Error("Password Details Not found!")
        } else {
            _screenState.value = ScreenState.Loading()
            viewModelScope.launch {
                _screenState.value = try {
                    val passwordDetails =
                        encryptedDataRepository.getEncryptedDataById(id = passwordId)
                    Log.d(
                        this@ViewPasswordDetailViewModel.javaClass.simpleName,
                        "EncryptedData :$passwordDetails"
                    )
                    ScreenState.Loaded(result = PasswordDetailResult.mockObject)
                } catch (e: Exception) {
                    e.printStackTrace()
                    ScreenState.Error("Unable to load password details")
                }
            }
        }

    }
}

data class PasswordDetailResult(
    val passwordId: Long,
    val passwordDetails: PasswordDetails,
    val createdAt: String,
    val modifiedAt: String
) {
    companion object {
        val mockObject = PasswordDetailResult(
            passwordId = 0L,
            passwordDetails = PasswordDetails.mockPasswordDetails,
            createdAt = "",
            modifiedAt = ""
        )
    }
}