package com.example.passvault.utils.extension_functions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.passvault.data.Vault
import com.example.passvault.ui.screens.main.vault_home.NavDrawerMenus
import com.example.passvault.utils.state.ScreenState

fun ImageVector.toIconKey(): String {
    return this.name
}

fun String.toOutlinedIcon(): ImageVector {
    return when (this) {
        Icons.Default.Home.name -> Icons.Default.Home
        Icons.Default.Person.name -> Icons.Default.Person
        Icons.Default.Language.name -> Icons.Default.Language
        Icons.Default.Work.name -> Icons.Default.Work
        Icons.Default.Lock.name -> Icons.Default.Lock
        Icons.Default.AttachMoney.name -> Icons.Default.AttachMoney
        Icons.Default.Favorite.name -> Icons.Default.Favorite
        Icons.Default.PermMedia.name -> Icons.Default.PermMedia
        else -> Icons.Default.Home
    }
}

fun NavDrawerMenus.toVault(): Vault? = if (this is NavDrawerMenus.VaultItem) this.vault else null

@Composable
fun <T> HandleScreenState(
    state: ScreenState<T>,
    onLoaded: @Composable (T) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is ScreenState.Error -> {
            // TODO: create error screen 
            Text(text = state.message ?: "Unable to load")
        }

        is ScreenState.Loaded -> onLoaded(state.result)
        is ScreenState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is ScreenState.PreLoad -> {}
    }
}