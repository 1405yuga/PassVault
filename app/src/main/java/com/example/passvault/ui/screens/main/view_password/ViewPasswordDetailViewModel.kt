package com.example.passvault.ui.screens.main.view_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.di.shared_reference.MasterCredentialsRepository
import com.example.passvault.network.supabase.EncryptedDataRepository
import com.example.passvault.utils.state.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    //Delete password------------------------------------------------------------------------
    var deletePasswordConfirmationDialog by mutableStateOf(false)
        private set

    fun showDeletePasswordConfirmation() {
        this.deletePasswordConfirmationDialog = true
    }

    fun hideDeletePasswordConfirmation() {
        this.deletePasswordConfirmationDialog = false
    }

    private val _deletePasswordScreenState =
        MutableStateFlow<ScreenState<Long>>(ScreenState.PreLoad())
    val deletePasswordScreenState: StateFlow<ScreenState<Long>> = _deletePasswordScreenState

    fun deletePassword(passwordId: Long) {
        _deletePasswordScreenState.value = ScreenState.Loading()
        viewModelScope.launch {
            _deletePasswordScreenState.value = try {
                val result =
                    encryptedDataRepository.deleteEncryptedDataById(passwordId = passwordId)
                if (result.isSuccess) ScreenState.Loaded(passwordId)
                else ScreenState.Error("Something went wrong")
            } catch (e: Exception) {
                e.printStackTrace()
                ScreenState.Error("Unable to delete")
            }
        }
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