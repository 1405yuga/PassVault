package com.example.passvault.ui.screens.main.add_vault

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passvault.R
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.example.passvault.utils.custom_composables.TextFieldWithErrorText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVaultDialog(
    addVaultViewModel: AddVaultViewModel,
    setShowDialog: (Boolean) -> Unit,
    onAddVaultClick: () -> Unit
) {
    Dialog(onDismissRequest = {
        setShowDialog(false)
        addVaultViewModel.clearInputs()
    }) {
        Surface(
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_radius)),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier
                .fillMaxWidth()
//                .padding(horizontal = dimensionResource(R.dimen.large_padding))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Add Vault",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
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
                    value = addVaultViewModel.vaultName,
                    onTextChange = { addVaultViewModel.onVaultNameChange(it) },
                    errorMsg = addVaultViewModel.vaultNameError
                )
                Spacer(modifier = Modifier.size(12.dp))
                IconSelector(
                    icons = addVaultViewModel.iconList,
                    onIconSelected = { addVaultViewModel.onIconSelected(it) },
                    selectedIcon = addVaultViewModel.currentSelectedIcon
                )
                Spacer(modifier = Modifier.size(24.dp))
                Button(
                    onClick = { onAddVaultClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(R.dimen.min_clickable_size)),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.button_radius))
                ) {
                    Text(text = "Add")
                }
            }
        }
    }
}

@Composable
fun IconSelector(
    icons: List<ImageVector>,
    onIconSelected: (ImageVector) -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: ImageVector = icons[0],
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
        addVaultViewModel = viewModel(),
        setShowDialog = {},
        onAddVaultClick = {},
    )
}

@Composable
@VerticalScreenPreview
fun IconSelectorPreview() {
    val iconList = listOf(
        Icons.Default.Home,
        Icons.Default.Person,
        Icons.Default.Lock,
        Icons.Default.Email,
        Icons.Default.Star,
        Icons.Default.Phone,
        Icons.Default.ShoppingCart,
        Icons.Default.Settings
    )

    IconSelector(
        icons = iconList,
//        selectedIcon = TODO(),
        onIconSelected = {},
    )
}