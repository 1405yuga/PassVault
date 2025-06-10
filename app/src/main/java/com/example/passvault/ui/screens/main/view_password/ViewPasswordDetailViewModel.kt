package com.example.passvault.ui.screens.main.view_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.data.CipherEncodedBundle
import com.example.passvault.data.MasterCredentials
import com.example.passvault.data.PasswordDetailResult
import com.example.passvault.data.PasswordDetails
import com.example.passvault.di.shared_reference.MasterCredentialsRepository
import com.example.passvault.network.supabase.EncryptedDataRepository
import com.example.passvault.utils.extension_functions.fromJsonString
import com.example.passvault.utils.helper.EncryptionHelper
import com.example.passvault.utils.state.ScreenState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ViewPasswordDetailViewModel @Inject constructor(
    private val encryptedDataRepository: EncryptedDataRepository,
    private val masterCredentialsRepository: MasterCredentialsRepository
) :
    ViewModel() {

//    private val _screenState =
//        MutableStateFlow<ScreenState<PasswordDetailResult>>(ScreenState.PreLoad())
//    val screenState: StateFlow<ScreenState<PasswordDetailResult>> = _screenState

    var showPassword by mutableStateOf(false)
        private set

    fun togglePasswordVisibility() {
        showPassword = !showPassword
    }

//    fun loadPasswordDetails(passwordId: Long?) {
//        if (passwordId == null) {
//            _screenState.value = ScreenState.Error("Password Details Not found!")
//        } else {
//            _screenState.value = ScreenState.Loading()
//            viewModelScope.launch {
//                _screenState.value = try {
//                    val encryptedData =
//                        encryptedDataRepository.getEncryptedDataById(id = passwordId)
//                    if (encryptedData == null) {
//                        ScreenState.Error("PasswordDetails Not Found")
//                    } else {
//                        //get masterCredentials
//                        val masterCredentialsJson =
//                            masterCredentialsRepository.getLocallyStoredMasterCredentialsJson()
//                        if (masterCredentialsJson == null) {
//                            ScreenState.Error(message = "Something went wrong. Login again!")
//                        } else {
//                            val masterCredentials: MasterCredentials =
//                                masterCredentialsJson.fromJsonString()
//                            val passwordDetails: PasswordDetails =
//                                withContext(Dispatchers.Default) {
//                                    val decryptedData: String = EncryptionHelper.performDecryption(
//                                        masterKey = masterCredentials.masterKey,
//                                        cipherEncodedBundle = CipherEncodedBundle(
//                                            encodedSalt = masterCredentials.encodedSalt,
//                                            encodedInitialisationVector = encryptedData.encodedInitialisationVector,
//                                            encodedEncryptedText = encryptedData.encodedEncryptedPasswordData
//                                        )
//                                    )
//                                    Gson().fromJson(decryptedData, PasswordDetails::class.java)
//                                }
//                            ScreenState.Loaded(
//                                result = PasswordDetailResult(
//                                    passwordId = passwordId,
//                                    passwordDetails = passwordDetails,
//                                    createdAt = encryptedData.createdAt ?: "",
//                                    modifiedAt = encryptedData.updatedAt ?: ""
//                                )
//                            )
//                        }
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    ScreenState.Error("Unable to load password details")
//                }
//            }
//        }
//
//    }
}