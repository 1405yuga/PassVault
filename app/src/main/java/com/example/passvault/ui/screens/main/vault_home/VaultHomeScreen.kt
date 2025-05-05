package com.example.passvault.ui.screens.main.vault_home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.passvault.ui.screens.main.add_vault.AddVaultDialog
import com.example.passvault.ui.screens.main.list.PasswordsListScreen
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultHomeScreen(
    toProfileScreen: () -> Unit,
    toAddPasswordScreen: () -> Unit,
    viewModel: VaultHomeViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
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
                viewModel.dummyVaultList.forEach {
                    val vaultMenu = NavDrawerMenus.VaultItem(vault = it)
                    NavigationDrawerItem(
                        label = {
                            Column {
                                Text(
                                    text = vaultMenu.label,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "0 items",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        selected = viewModel.lastVaultMenu == vaultMenu,
                        onClick = {
                            viewModel.onMenuSelected(vaultMenu)
                            scope.launch { drawerState.close() }
                        },
                        icon = { Icon(vaultMenu.icon, contentDescription = null) },
                        modifier = Modifier.padding(
                            start = dimensionResource(R.dimen.medium_padding),
                            end = dimensionResource(R.dimen.medium_padding),
                            bottom = dimensionResource(R.dimen.medium_padding)
                        ),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.button_radius))
                    )

                }
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = NavDrawerMenus.AddVault.label,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    selected = false,
                    onClick = {
                        viewModel.onMenuSelected(NavDrawerMenus.AddVault)
//                        scope.launch { drawerState.close() }
                        viewModel.toggleCreateVaultDialog(showDialog = true)
                    },
                    icon = { Icon(NavDrawerMenus.AddVault.icon, contentDescription = null) },
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
                            text = NavDrawerMenus.Profile.label,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    selected = false,
                    onClick = {
                        viewModel.onMenuSelected(NavDrawerMenus.Profile)
                        scope.launch { drawerState.close() }
                        toProfileScreen()
                    },
                    icon = { Icon(NavDrawerMenus.Profile.icon, contentDescription = null) },
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
                        }) { Icon(viewModel.lastVaultMenu.icon, contentDescription = "Menu") }
                    })
            },
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(R.dimen.medium_padding))
                ) {
                    // TODO: load screen when vault menu clicked
                    PasswordsListScreen(
                        onAddClick = {
                            toAddPasswordScreen()
                        })
                    if (viewModel.showCreateVaultDialog) {
                        AddVaultDialog(
                            addVaultViewModel = viewModel(),
                            setShowDialog = { viewModel.toggleCreateVaultDialog(it) },
                        )
                    }
                }
            }
        )
    }
}

@Composable
@VerticalScreenPreview
fun VaultHomeScreenPreview() {
    VaultHomeScreen(
        viewModel = viewModel(),
        toProfileScreen = {},
        toAddPasswordScreen = {},
    )
}

@Composable
@HorizontalScreenPreview
fun VaultHomeScreenHorizontalPreview() {
    VaultHomeScreen(
        viewModel = viewModel(),
        toProfileScreen = {},
        toAddPasswordScreen = {},
    )
}