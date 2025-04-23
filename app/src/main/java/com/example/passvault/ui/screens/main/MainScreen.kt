package com.example.passvault.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.passvault.ui.screens.main.add.AddPasswordScreen
import com.example.passvault.ui.screens.main.list.PasswordsListScreen
import com.example.passvault.ui.screens.main.profile.ProfileScreen
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val items = listOf("List", "Add", "Profile")
    val iconsList = listOf(
        Icons.AutoMirrored.Outlined.List,
        Icons.Outlined.Add,
        Icons.Filled.Person
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            //todo: add animations on switch
            when (selectedItem) {
                0 -> PasswordsListScreen()
                1 -> AddPasswordScreen()
                2 -> ProfileScreen()
            }
        }
        NavigationBar(modifier = Modifier.fillMaxWidth()) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(iconsList[index], contentDescription = item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index }
                )
            }
        }
    }
}

@Composable
@VerticalScreenPreview
private fun MainScreenPreview() {
    MainScreen()
}

@Composable
@HorizontalScreenPreview
private fun MainScreenHorizontalPreview() {
    MainScreen()
}