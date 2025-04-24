package com.example.passvault.ui.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel

class MainScreenViewModel : ViewModel() {

    var currentSelectedMenu by mutableStateOf<MainScreenMenus>(MainScreenMenus.List)
        private set

    val mainScreenMenusItems = listOf<MainScreenMenus>(
        MainScreenMenus.List,
        MainScreenMenus.Add,
        MainScreenMenus.Profile
    )

    fun onMenuSelected(mainScreenMenus: MainScreenMenus) {
        currentSelectedMenu = mainScreenMenus
    }
}

sealed class MainScreenMenus(val route: String, val icon: ImageVector) {
    object List : MainScreenMenus(route = "List", icon = Icons.AutoMirrored.Outlined.List)
    object Add : MainScreenMenus(route = "Add", icon = Icons.Outlined.Add)
    object Profile : MainScreenMenus(route = "Profile", icon = Icons.Filled.Person)
}