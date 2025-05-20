package com.example.passvault.ui.screens.main.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.data.Vault
import com.example.passvault.network.supabase.VaultRepository
import com.example.passvault.utils.state.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPasswordViewModel @Inject constructor(private val vaultRepository: VaultRepository) :
    ViewModel() {

    private var _vaultListScreenState =
        MutableStateFlow<ScreenState<List<Vault>>>(ScreenState.PreLoad())
    val vaultListScreenState: StateFlow<ScreenState<List<Vault>>> = _vaultListScreenState

    init {
        loadVaults()
    }

    fun loadVaults() {
        _vaultListScreenState.value = ScreenState.Loading()
        try {
            viewModelScope.launch {
                val result = vaultRepository.getAllVaults()
                _vaultListScreenState.value = ScreenState.Loaded(result = result)
                onSelectedVaultChange(vault = result.first())
            }

        } catch (e: Exception) {
            e.printStackTrace()
            _vaultListScreenState.value = ScreenState.Error(message = "Unable to load Vaults")
        }
    }

    var title by mutableStateOf("")
        private set
    var username by mutableStateOf("") //optional
        private set
    var password by mutableStateOf("")
        private set
    var showPassword by mutableStateOf(false)
        private set
    var website by mutableStateOf("") //optional
        private set
    var notes by mutableStateOf("") //optional
        private set
    var vaultMenuExpanded by mutableStateOf(false)
        private set
    var selectedVault by mutableStateOf(Vault.defaultVault())
        private set

    fun onTitleChange(title: String) {
        this.title = title
    }

    fun onUsernameChange(username: String) {
        this.username = username
    }

    fun onPasswordChange(password: String) {
        this.password = password
    }

    fun togglePasswordVisibility() {
        showPassword = !showPassword
    }

    fun onWebsiteChange(website: String) {
        this.website = website
    }

    fun onNotesChange(notes: String) {
        this.notes = notes
    }

    fun toggleVaultMenuExpantion() {
        this.vaultMenuExpanded = !this.vaultMenuExpanded
    }

    fun onSelectedVaultChange(vault: Vault) {
        this.selectedVault = vault
    }

    var titleError by mutableStateOf("")
        private set

    var passwordError by mutableStateOf("")
        private set

    private fun areInputsValid(): Boolean {
        titleError = if (title.trim().isBlank()) "Title is required" else ""
        passwordError = if (password.trim().isBlank()) "Password is required" else ""
        return titleError.isBlank()
    }

    fun createPassword() {
        if (!areInputsValid()) return
    }
}