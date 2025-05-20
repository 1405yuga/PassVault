package com.example.passvault.ui.screens.main.add

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.passvault.R
import com.example.passvault.data.Vault
import com.example.passvault.di.supabase.SupabaseModule
import com.example.passvault.network.supabase.VaultRepository
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.example.passvault.utils.custom_composables.ShowAndHidePasswordTextField
import com.example.passvault.utils.custom_composables.TextFieldWithErrorText
import com.example.passvault.utils.extension_functions.HandleScreenState
import com.example.passvault.utils.extension_functions.toOutlinedIcon

@Composable
fun AddPasswordScreen(
    onClose: () -> Unit, viewModel: AddPasswordViewModel, modifier: Modifier = Modifier
) {
    val vaultsState by viewModel.vaultListScreenState.collectAsState()
    HandleScreenState(state = vaultsState, onLoaded = {
        AddPasswordDetailsScreen(
            onClose = onClose,
            viewModel = viewModel,
            modifier = modifier,
            vaults = it
        )
    })
}

@Composable
fun AddPasswordDetailsScreen(
    onClose: () -> Unit,
    viewModel: AddPasswordViewModel,
    vaults: List<Vault>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = dimensionResource(R.dimen.large_padding),
                vertical = dimensionResource(R.dimen.medium_padding)
            )
            .verticalScroll(rememberScrollState())
            .imePadding()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            IconButton(
                onClick = {
// TODO: dialog of confirmation
                    onClose()
                }, modifier = Modifier.size(dimensionResource(R.dimen.min_clickable_size))
            ) {
                Icon(
                    Icons.Outlined.Clear,
                    contentDescription = "close window",
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box {
                Button(
                    onClick = {
                        // TODO: get vault list
                        viewModel.toggleVaultMenuExpantion()
                    },
                    modifier = Modifier.height(dimensionResource(R.dimen.min_clickable_size)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Icon(
                        imageVector = viewModel.selectedVault.iconKey.toOutlinedIcon(),
                        contentDescription = viewModel.selectedVault.iconKey
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = viewModel.selectedVault.vaultName)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = "Get vault")
                }
                VaultDropDownMenu(
                    vaults = vaults,
                    vaultDropDownExpanded = viewModel.vaultMenuExpanded,
                    onMenuDismiss = { viewModel.toggleVaultMenuExpantion() },
                    onVaultClick = {
                        // TODO: get vault
                        viewModel.onSelectedVaultChange(it)
                        viewModel.toggleVaultMenuExpantion()
                    },
                    selectedVault = viewModel.selectedVault
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Button(
                onClick = { viewModel.createPassword() },
                modifier = Modifier.height(dimensionResource(R.dimen.min_clickable_size))
            ) { Text(text = "Create") }
        }
        TextFieldWithErrorText(
            label = "Title",
            value = viewModel.title,
            onTextChange = { viewModel.onTitleChange(it) },
            errorMsg = viewModel.titleError
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_spacer_height)))
        OutlinedTextField(
            label = { Text("Username or email") },
            value = viewModel.username,
            onValueChange = { viewModel.onUsernameChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.AccountBox, contentDescription = "Username") })

        ShowAndHidePasswordTextField(
            label = "Password",
            password = viewModel.password,
            onTextChange = { viewModel.onPasswordChange(it) },
            showPassword = viewModel.showPassword,
            onShowPasswordClick = { viewModel.togglePasswordVisibility() },
            errorMsg = viewModel.passwordError,
            leadingIcon = Icons.Outlined.Password
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_spacer_height)))
        OutlinedTextField(
            label = { Text("Website") },
            value = viewModel.website,
            onValueChange = { viewModel.onWebsiteChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Language, contentDescription = "Website") })
        OutlinedTextField(
            label = { Text("Notes") },
            value = viewModel.notes,
            onValueChange = { viewModel.onNotesChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.Description, contentDescription = "Notes") })
    }
}

@Composable
fun VaultDropDownMenu(
    vaults: List<Vault>,
    vaultDropDownExpanded: Boolean,
    onMenuDismiss: () -> Unit,
    onVaultClick: (vault: Vault) -> Unit,
    selectedVault: Vault,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = vaultDropDownExpanded,
        onDismissRequest = onMenuDismiss,
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        vaults.forEach { option ->
            DropdownMenuItem(
                text = { Text(option.vaultName) },
                leadingIcon = {
                    Icon(
                        imageVector = option.iconKey.toOutlinedIcon(),
                        contentDescription = null
                    )
                },
                onClick = {
                    // TODO:
                    onVaultClick(option)
                },
                trailingIcon = {
                    if (selectedVault == option) {
                        Icon(Icons.Outlined.Check, contentDescription = "Selected vault")
                    } else {
                        null
                    }
                })
        }
    }
}

@Composable
@VerticalScreenPreview
private fun AddPasswordScreenPreview() {
    AddPasswordDetailsScreen(
        onClose = {}, viewModel = AddPasswordViewModel(
            vaultRepository = VaultRepository(SupabaseModule.mockClient)
        ),
        vaults = List(5) {
            Vault(
                vaultId = it.toLong(),
                userId = "someUser",
                vaultName = "Vault name",
                iconKey = Icons.Outlined.Home.name,
                createdAt = ""
            )
        })
}

@Composable
@HorizontalScreenPreview
private fun AddPasswordScreenHorizontalPreview() {
    AddPasswordDetailsScreen(
        onClose = {}, viewModel = AddPasswordViewModel(
            vaultRepository = VaultRepository(SupabaseModule.mockClient)
        ),
        vaults = List(5) {
            Vault(
                vaultId = it.toLong(),
                userId = "someUser",
                vaultName = "Vault name",
                iconKey = Icons.Outlined.Home.name,
                createdAt = ""
            )
        }
    )
}