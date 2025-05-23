package com.example.passvault.ui.screens.main.vault_home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Menu
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.passvault.R
import com.example.passvault.di.supabase.SupabaseModule
import com.example.passvault.network.supabase.VaultRepository
import com.example.passvault.ui.screens.main.add_vault.AddVaultDialog
import com.example.passvault.ui.screens.main.list.PasswordsListScreen
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.example.passvault.utils.custom_composables.ConfirmationAlertDialog
import com.example.passvault.utils.extension_functions.HandleScreenState
import com.example.passvault.utils.extension_functions.toOutlinedIcon
import com.example.passvault.utils.extension_functions.toVault
import com.google.gson.Gson
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultHomeScreen(
    toProfileScreen: () -> Unit,
    toAddPasswordScreen: (vaultString: String) -> Unit,
    viewModel: VaultHomeViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()
    val vaultScreenState by viewModel.vaultScreenState.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
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
                    HandleScreenState(
                        state = vaultScreenState,
                        onLoaded = { vaultList ->
                            if (vaultList.isEmpty()) {
                                // TODO: create ui to display below message
                                Text(text = "Add vaults to get started!")
                            } else {
                                vaultList.forEach { vault ->
                                    val vaultMenu = NavDrawerMenus.VaultItem(vault = vault)
                                    NavigationDrawerItem(
                                        label = {
                                            Row(modifier = Modifier.fillMaxWidth()) {
                                                Column(modifier = Modifier.weight(1f)) {
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

                                                //atleast one vault required
                                                if (vaultList.size > 1) {
                                                    IconButton(
                                                        onClick = {
                                                            viewModel.showRemoveConfirmation(vault = vault)
                                                        }
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Outlined.DeleteOutline,
                                                            contentDescription = "Remove Vault",
                                                            tint = MaterialTheme.colorScheme.error
                                                        )
                                                    }
                                                }
                                            }

                                        },
                                        selected = vaultMenu == (viewModel.lastVaultMenu ?: false),
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
                            }
                        })
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
                            viewModel.toggleCreateVaultDialog(openDialog = true)
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
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = viewModel.lastVaultMenu?.label
                                ?: stringResource(R.string.app_name)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = viewModel.lastVaultMenu?.icon ?: Icons.Outlined.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    })
            },
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = dimensionResource(R.dimen.medium_padding))
                ) {
                    //  load screen when vault menu clicked
                    PasswordsListScreen(
                        onAddClick = {
                            viewModel.lastVaultMenu?.let {
                                toAddPasswordScreen(Gson().toJson(it.toVault()))
                            }
                        })

                    // loadDialogs
                    when {
                        viewModel.openAddVaultDialog -> {
                            AddVaultDialog(
                                dialogState = viewModel.addVaultDialogState,
                                onVaultNameChange = { viewModel.onVaultNameChange(it) },
                                onIconSelected = { viewModel.onIconSelected(it) },
                                insertNewVault = { viewModel.addNewVault() },
                                setShowDialog = { viewModel.toggleCreateVaultDialog(it) }
                            )
                        }

                        viewModel.openRemoveVaultConfirmationDialog -> {
                            ConfirmationAlertDialog(
                                onDismissRequest = { viewModel.closeRemoveDialog() },
                                onConfirmation = { viewModel.removeVault() },
                                dialogTitle = "Delete ${viewModel.vaultToBeRemoved?.vaultName}",
                                dialogText = "Deleting this vault deletes all its passwords permanently.\nAre you sure to proceed?",
                                icon = viewModel.vaultToBeRemoved?.iconKey?.toOutlinedIcon()
                            )
                        }
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
        viewModel = VaultHomeViewModel(
            vaultRepository = VaultRepository(SupabaseModule.mockClient)
        ),
        toProfileScreen = {},
        toAddPasswordScreen = {},
    )
}

@Composable
@HorizontalScreenPreview
fun VaultHomeScreenHorizontalPreview() {
    VaultHomeScreen(
        viewModel = VaultHomeViewModel(
            vaultRepository = VaultRepository(SupabaseModule.mockClient)
        ),
        toProfileScreen = {},
        toAddPasswordScreen = {},
    )
}

@Composable
@VerticalScreenPreview
fun MenuPreview() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Vault name",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "0 items",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Outlined.DeleteOutline,
                contentDescription = "More Options"
            )
        }
    }

}