package com.example.passvault.ui.screens.main

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passvault.data.Vault
import com.example.passvault.network.supabase.VaultRepository
import com.example.passvault.ui.screens.main.nav_drawer.NavDrawerMenus
import com.example.passvault.utils.state.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val vaultRepository: VaultRepository) :
    ViewModel() {

    private var _vaultListScreenState =
        MutableStateFlow<ScreenState<List<Vault>>>(ScreenState.PreLoad())
    val vaultScreenState: StateFlow<ScreenState<List<Vault>>> = _vaultListScreenState

    private var _vaultList = MutableStateFlow<List<Vault>>(emptyList())
    var vaultList: StateFlow<List<Vault>> = _vaultList

    var currentSelectedMenu by mutableStateOf<NavDrawerMenus?>(null)
        private set

    var lastVaultMenu by mutableStateOf<NavDrawerMenus?>(currentSelectedMenu)
        private set

    fun onMenuSelected(navDrawerMenus: NavDrawerMenus) {
        if (navDrawerMenus is NavDrawerMenus.VaultItem) {
            Log.d(this.javaClass.simpleName, "Vault menu : $navDrawerMenus")
            lastVaultMenu = navDrawerMenus
        }
        currentSelectedMenu = navDrawerMenus
    }

    init {
        getVaults(isInitialLoad = true)
    }

    fun addVaultToList(vault: Vault) {
        _vaultList.value = _vaultList.value + vault
        onMenuSelected(NavDrawerMenus.VaultItem(vault))
    }

    fun removeVaultFromListById(vaultId: Long?) {
        vaultId?.let { id ->
            _vaultList.value = _vaultList.value.filter { id != it.vaultId }
        }
        onMenuSelected(NavDrawerMenus.VaultItem(_vaultList.value.last()))
    }

    fun getVaults(isInitialLoad: Boolean? = null) {
        _vaultListScreenState.value = ScreenState.Loading()
        try {
            viewModelScope.launch {
                val result = vaultRepository.getAllVaults()
                Log.d(this@MainScreenViewModel.javaClass.simpleName, "Vaults : ${result.size}")
                _vaultList.value = result
                if (result.isNotEmpty()) {
                    when (isInitialLoad) {
                        true -> onMenuSelected(
                            navDrawerMenus = NavDrawerMenus.VaultItem(vault = result.first())
                        )

                        false -> onMenuSelected(
                            navDrawerMenus = NavDrawerMenus.VaultItem(vault = result.last())
                        )

                        null -> {
                            if (result.size == 1) {
                                onMenuSelected(
                                    navDrawerMenus = NavDrawerMenus.VaultItem(vault = result.first())
                                )
                            }
                        }
                    }
                }
                _vaultListScreenState.value = ScreenState.Loaded(result = result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _vaultListScreenState.value = ScreenState.Error(message = "Unable to load Vaults")
        }
    }
}

sealed class MainScreens(val route: String) {
    object VaultHome : MainScreens(route = "VaultHome")
    object AddPassword : MainScreens(route = "AddPassword/{passwordDataWithId}") {
        const val initialPasswordData = "passwordDataWithId"
        fun createRoute(passwordDetailsResultString: String) =
            "AddPassword/$passwordDetailsResultString"
    }

    object Profile : MainScreens(route = "Profile")
    object ViewPassword : MainScreens(route = "ViewPassword/{passwordId}") {
        const val argumentName = "passwordId"
        fun createRoute(id: Long?) = "ViewPassword/$id"
    }
}