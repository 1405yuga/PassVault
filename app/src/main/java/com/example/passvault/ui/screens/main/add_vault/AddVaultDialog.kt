package com.example.passvault.ui.screens.main.add_vault

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
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
    setShowDialog: (Boolean) -> Unit
) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_radius)),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Create new Vault")
                TextFieldWithErrorText(
                    label = "Vault name",
                    value = addVaultViewModel.vaultName,
                    onTextChange = { addVaultViewModel.onVaultNameChange(it) },
                    errorMsg = addVaultViewModel.vaultNameError
                )
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
    )
}