package com.example.passvault.ui.screens.main.add_password

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.passvault.R
import com.example.passvault.data.Vault
import com.example.passvault.di.shared_reference.EncryptedPrefsModule
import com.example.passvault.di.shared_reference.MasterCredentialsRepository
import com.example.passvault.di.supabase.SupabaseModule
import com.example.passvault.network.supabase.EncryptedDataRepository
import com.example.passvault.network.supabase.VaultRepository
import com.example.passvault.ui.screens.main.view_password.PasswordDetailResult
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.example.passvault.utils.custom_composables.ShowAndHidePasswordTextField
import com.example.passvault.utils.custom_composables.TextFieldWithErrorText
import com.example.passvault.utils.extension_functions.HandleScreenState
import com.example.passvault.utils.extension_functions.toImageVector
import com.example.passvault.utils.state.ScreenState

@Composable
fun UpsertPasswordDetailScreen(
    onClose: () -> Unit,
    selectedVault: Vault?,
    onUpdateSelectedVault: (Vault) -> Unit,
    viewModel: UpsertPasswordDetailViewModel,
    passwordDetailResult: PasswordDetailResult?,
    modifier: Modifier = Modifier
) {
    val vaultsState by viewModel.vaultListScreenState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadInitialData(passwordDetails = passwordDetailResult?.passwordDetails)
    }
    HandleScreenState(state = vaultsState, onLoaded = {
        UpsertScreen(
            onClose = onClose,
            viewModel = viewModel,
            selectedVault = selectedVault,
            onUpdateSelectedVault = { onUpdateSelectedVault(it) },
            storeButtonLable = if (passwordDetailResult == null) "Create" else "Update",
            modifier = modifier,
            vaults = it,
            passwordId = passwordDetailResult?.passwordId
        )
    })
}

@Composable
fun UpsertScreen(
    onClose: () -> Unit,
    viewModel: UpsertPasswordDetailViewModel,
    selectedVault: Vault?,
    onUpdateSelectedVault: (Vault) -> Unit,
    vaults: List<Vault>,
    storeButtonLable: String,
    passwordId: Long?,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) { if (selectedVault != null) viewModel.onSelectedVaultChange(selectedVault) }
    val screenState by viewModel.screenState.collectAsState()
    val context = LocalContext.current

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
        LaunchedEffect(screenState) {
            when (val state = screenState) {
                is ScreenState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }

                is ScreenState.Loaded -> {
                    onClose()
                }

                else -> {}
            }
        }
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
                        imageVector = viewModel.selectedVault.iconKey.toImageVector(),
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
                        onUpdateSelectedVault(it)
                        viewModel.onSelectedVaultChange(it)
                        viewModel.toggleVaultMenuExpantion()
                    },
                    selectedVault = viewModel.selectedVault
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Button(
                onClick = { viewModel.storePasswordDetails(passwordId = passwordId) },
                modifier = Modifier.height(dimensionResource(R.dimen.min_clickable_size)),
                enabled = screenState !is ScreenState.Loading
            ) {
                when (screenState) {
                    is ScreenState.Loading -> Text(text = "Loading..")
                    else -> Text(text = storeButtonLable)
                }
            }
        }
        TextFieldWithErrorText(
            label = "Title",
            value = viewModel.title,
            onTextChange = { viewModel.onTitleChange(it) },
            errorMsg = viewModel.titleError
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_spacer_height)))
        OutlinedTextField(
            label = { Text(text = stringResource(R.string.username_or_email)) },
            value = viewModel.username,
            onValueChange = { viewModel.onUsernameChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.AccountBox, contentDescription = "Username") })

        ShowAndHidePasswordTextField(
            label = stringResource(R.string.password),
            password = viewModel.password,
            onTextChange = { viewModel.onPasswordChange(it) },
            showPassword = viewModel.showPassword,
            onShowPasswordClick = { viewModel.togglePasswordVisibility() },
            errorMsg = viewModel.passwordError,
            leadingIcon = Icons.Outlined.Password
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_spacer_height)))
        OutlinedTextField(
            label = { Text(stringResource(R.string.website)) },
            value = viewModel.website,
            onValueChange = { viewModel.onWebsiteChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Language, contentDescription = "Website") })
        OutlinedTextField(
            label = { Text(stringResource(R.string.notes)) },
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
                        imageVector = option.iconKey.toImageVector(),
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
    val vaults = List(5) {
        Vault(
            vaultId = it.toLong(),
            userId = "someUser",
            vaultName = "Vault name",
            iconKey = Icons.Outlined.Home.name,
            createdAt = ""
        )
    }
    UpsertScreen(
        onClose = {},
        viewModel = UpsertPasswordDetailViewModel(
            vaultRepository = VaultRepository(SupabaseModule.mockClient),
            masterCredentialsRepository = MasterCredentialsRepository(
                encryptedPrefs = EncryptedPrefsModule.mockSharedPreference
            ),
            encryptedDataRepository = EncryptedDataRepository(SupabaseModule.mockClient),
        ),
        vaults = vaults,
        selectedVault = vaults.first(),
        storeButtonLable = "Create",
        passwordId = 0L,
        onUpdateSelectedVault = {}
    )
}

@Composable
@HorizontalScreenPreview
private fun AddPasswordScreenHorizontalPreview() {
    val vaults = List(5) {
        Vault(
            vaultId = it.toLong(),
            userId = "someUser",
            vaultName = "Vault name",
            iconKey = Icons.Outlined.Home.name,
            createdAt = ""
        )
    }
    UpsertScreen(
        onClose = {},
        viewModel = UpsertPasswordDetailViewModel(
            vaultRepository = VaultRepository(SupabaseModule.mockClient),
            masterCredentialsRepository = MasterCredentialsRepository(
                encryptedPrefs = EncryptedPrefsModule.mockSharedPreference
            ), encryptedDataRepository = EncryptedDataRepository(SupabaseModule.mockClient)
        ),
        vaults = vaults,
        selectedVault = vaults.first(),
        storeButtonLable = "Create",
        passwordId = 0L,
        onUpdateSelectedVault = {}
    )
}