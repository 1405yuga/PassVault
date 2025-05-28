package com.example.passvault.ui.screens.main.add_vault

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.passvault.R
import com.example.passvault.di.supabase.SupabaseModule
import com.example.passvault.network.supabase.VaultRepository
import com.example.passvault.ui.screens.main.vault_home.VaultHomeViewModel
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.example.passvault.utils.custom_composables.TextFieldWithErrorText
import com.example.passvault.utils.helper.VaultIconsList
import com.example.passvault.utils.state.ScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVaultDialog(
    vaultViewModel: VaultHomeViewModel,
    onVaultNameChange: (String) -> Unit,
    onIconSelected: (ImageVector) -> Unit,
    insertNewVault: () -> Unit,
    setShowDialog: (Boolean) -> Unit,
) {
    val currentContext = LocalContext.current
    val screenState by vaultViewModel.addDialogScreenState.collectAsState()

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_radius)),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
                    .height(400.dp)
            ) {
                Text(
                    text = "Add Vault",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "Organize passwords securely",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.size(12.dp))
                TextFieldWithErrorText(
                    label = "Vault name",
                    value = vaultViewModel.vaultName,
                    onTextChange = { onVaultNameChange(it) },
                    errorMsg = vaultViewModel.vaultError
                )
                Spacer(modifier = Modifier.size(12.dp))
                IconSelector(
                    icons = VaultIconsList.getIconsList(),
                    onIconSelected = { onIconSelected(it) },
                    selectedIcon = vaultViewModel.iconSelected
                )
                Spacer(modifier = Modifier.size(24.dp))
                Button(
                    onClick = {
                        insertNewVault()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.min_clickable_size))
                        .widthIn(min = dimensionResource(R.dimen.min_clickable_size)),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.button_radius)),
                    enabled = screenState !is ScreenState.Loading
                ) {
                    Text(
                        text =
                            if (screenState is ScreenState.Loading) "Loading.." else "Add"
                    )
                }
            }
        }
        LaunchedEffect(screenState) {
            when (val state = screenState) {
                is ScreenState.Error -> {
                    Toast.makeText(currentContext, state.message, Toast.LENGTH_SHORT).show()
                }

                is ScreenState.Loaded -> {
                    Toast.makeText(currentContext, "Added Vault successfully", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {}
            }
        }
    }
}

@Composable
fun IconSelector(
    icons: List<ImageVector>,
    onIconSelected: (ImageVector) -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: ImageVector? = icons[0],
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = true
        ) {
            items(icons) { icon ->
                IconButton(
                    onClick = { onIconSelected(icon) },
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.min_clickable_size))
                        .background(
                            if (icon == selectedIcon) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            else Color.Transparent,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (icon == selectedIcon) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
@VerticalScreenPreview
fun AddVaultDialogPreview() {
    AddVaultDialog(
        vaultViewModel = VaultHomeViewModel(
            vaultRepository = VaultRepository(
                supabaseClient = SupabaseModule.mockClient
            )
        ),
        onVaultNameChange = {},
        onIconSelected = {},
        insertNewVault = {},
        setShowDialog = {}
    )
}

@Composable
@HorizontalScreenPreview
fun AddVaultDialogHorizontalPreview() {
    AddVaultDialog(
        vaultViewModel = VaultHomeViewModel(
            vaultRepository = VaultRepository(
                supabaseClient = SupabaseModule.mockClient
            )
        ),
        onVaultNameChange = {},
        onIconSelected = {},
        insertNewVault = {},
        setShowDialog = {}
    )
}

@Composable
@VerticalScreenPreview
fun IconSelectorPreview() {
    val iconList = listOf(
        Icons.Default.Home,
        Icons.Default.Person,
        Icons.Default.Language,
        Icons.Default.Work,
        Icons.Default.Lock,
        Icons.Default.AttachMoney,
        Icons.Default.Favorite,
        Icons.Default.PermMedia,
    )

    IconSelector(
        icons = iconList,
//        selectedIcon = TODO(),
        onIconSelected = {},
    )
}