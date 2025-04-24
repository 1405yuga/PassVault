package com.example.passvault.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passvault.R
import com.example.passvault.ui.screens.main.add.AddPasswordBottomSheet
import com.example.passvault.ui.screens.main.list.PasswordsListScreen
import com.example.passvault.ui.screens.main.profile.ProfileScreen
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    Scaffold(
        bottomBar = {
            NavigationBar(modifier = Modifier.fillMaxWidth()) {
                viewModel.mainScreenMenusItems.forEach { menuItem ->
                    NavigationBarItem(
                        icon = { Icon(menuItem.icon, contentDescription = menuItem.route) },
                        selected = viewModel.currentSelectedMenu == menuItem,
                        onClick = { viewModel.onMenuSelected(menuItem) }
                    )
                }
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = dimensionResource(R.dimen.medium_padding))
            ) {
                //todo: add animations on switch
                when (viewModel.lastNonAddMenu) {
                    MainScreenMenus.List -> PasswordsListScreen()
                    MainScreenMenus.Profile -> ProfileScreen()
                    else -> {}
                }

                if (viewModel.currentSelectedMenu == MainScreenMenus.Add) {
                    AddPasswordBottomSheet(onDismiss = {
                        viewModel.onAddPasswordBottomSheetDismiss()
                    })
                }
            }
        }
    )
}

@Composable
@VerticalScreenPreview
private fun MainScreenPreview() {
    MainScreen(viewModel = viewModel())
}

@Composable
@HorizontalScreenPreview
private fun MainScreenHorizontalPreview() {
    MainScreen(viewModel = viewModel())
}