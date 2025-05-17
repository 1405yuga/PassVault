package com.example.passvault.ui.screens.main.add_vault

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
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
class AddVaultViewModel @Inject constructor(private val vaultRepository: VaultRepository) :
    ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState<String>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<String>> = _screenState

    var vaultName by mutableStateOf("")
        private set

    fun onVaultNameChange(vaultName: String) {
        this.vaultName = vaultName
    }

    var vaultNameError by mutableStateOf("")
        private set
    var currentSelectedIcon by mutableStateOf(IconsList.getIconsList()[0])
        private set

    fun onIconSelected(icon: ImageVector) {
        this.currentSelectedIcon = icon
    }

    private fun checkInputFields(): Boolean {
        vaultNameError = if (vaultName.trim().isBlank()) "Vault Name is required" else ""
        return vaultNameError.isBlank()
    }

    fun addNewVault() {
        if (!checkInputFields()) return
        _screenState.value = ScreenState.Loading()
        viewModelScope.launch {
            _screenState.value = try {
                vaultRepository.insertVault(
                    vault = Vault(
                        vaultName = this@AddVaultViewModel.vaultName,
                        iconKey = this@AddVaultViewModel.currentSelectedIcon.name
                    )
                )
                ScreenState.Loaded("Added vault successfully")
            } catch (e: Exception) {
                e.printStackTrace()
                ScreenState.Error("Unable to add vault")
            }
        }
    }

    fun clearInputs() {
        vaultName = ""
        vaultNameError = ""
        currentSelectedIcon = IconsList.getIconsList()[0]
        _screenState.value = ScreenState.PreLoad()
    }
}

object IconsList {
    fun getIconsList(): List<ImageVector> = listOf(
        Icons.Default.Home,
        Icons.Default.Person,
        Icons.Default.Language,
        Icons.Default.Work,
        Icons.Default.Lock,
        Icons.Default.AttachMoney,
        Icons.Default.Favorite,
        Icons.Default.PermMedia,
    )
}