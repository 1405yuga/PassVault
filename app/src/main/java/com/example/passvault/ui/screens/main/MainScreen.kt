package com.example.passvault.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passvault.R
import com.example.passvault.ui.screens.main.add.AddPasswordBottomSheet
import com.example.passvault.ui.screens.main.list.PasswordsListScreen
import com.example.passvault.ui.screens.main.profile.ProfileScreen
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(dimensionResource(R.dimen.medium_padding))
                )
                HorizontalDivider()
                Text(
                    text = "Vaults",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.menu_medium_padding),
                        top = dimensionResource(R.dimen.menu_medium_padding),
                        bottom = dimensionResource(R.dimen.menu_small_padding)
                    )
                )
                NavigationDrawerItem(
                    label = {
                        Column {
                            Text(
                                text = MainScreenMenus.List.route,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "0 items",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    selected = viewModel.currentSelectedMenu == MainScreenMenus.List,
                    onClick = {
                        viewModel.onMenuSelected(MainScreenMenus.List)
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(MainScreenMenus.List.icon, contentDescription = null) },
                    modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.medium_padding),
                        end = dimensionResource(R.dimen.medium_padding),
                        bottom = dimensionResource(R.dimen.medium_padding)
                    ),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.button_radius))
                )
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = MainScreenMenus.AddVault.route,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    selected = false,
                    onClick = {
                        viewModel.onMenuSelected(MainScreenMenus.AddVault)
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(MainScreenMenus.AddVault.icon, contentDescription = null) },
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(R.dimen.medium_padding),
                            vertical = dimensionResource(R.dimen.small_padding)
                        )
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.button_radius))
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = MainScreenMenus.Profile.route,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    selected = viewModel.currentSelectedMenu == MainScreenMenus.Profile,
                    onClick = {
                        viewModel.onMenuSelected(MainScreenMenus.Profile)
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(MainScreenMenus.Profile.icon, contentDescription = null) },
                    modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.medium_padding),
                        end = dimensionResource(R.dimen.medium_padding),
                        bottom = dimensionResource(R.dimen.medium_padding),
                        top = dimensionResource(R.dimen.medium_padding)
                    ),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.button_radius))
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(R.string.app_name),
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) { Icon(Icons.Default.Menu, contentDescription = "Menu") }
                    })
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