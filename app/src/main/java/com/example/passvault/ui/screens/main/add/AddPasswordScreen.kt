package com.example.passvault.ui.screens.main.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passvault.R
import com.example.passvault.ui.screens.authentication.signup.ShowAndHidePasswordTextField
import com.example.passvault.ui.screens.authentication.signup.TextFieldWithErrorText
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPasswordBottomSheet(
    onDismiss: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = bottomSheetState,
        dragHandle = null,
        content = {
            AddPasswordScreen(
                onClose = onDismiss,
                viewModel = viewModel()
            )
        }
    )
}

@Composable
fun AddPasswordScreen(
    onClose: () -> Unit,
    viewModel: AddPasswordViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = dimensionResource(R.dimen.large_padding),
                vertical = dimensionResource(R.dimen.medium_padding)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.min_clickable_size))
            ) {
                Icon(
                    Icons.Outlined.Clear,
                    contentDescription = "close window",
                )
            }
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
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            label = { Text("Username") },
            value = viewModel.username,
            onValueChange = { viewModel.onUsernameChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        ShowAndHidePasswordTextField(
            label = "Password",
            password = viewModel.password,
            onTextChange = { viewModel.onPasswordChange(it) },
            showPassword = viewModel.showPassword,
            onShowPasswordClick = { viewModel.togglePasswordVisibility() },
            errorMsg = viewModel.passwordError
        )
    }
}

@Composable
@VerticalScreenPreview
private fun AddPasswordScreenPreview() {
    AddPasswordScreen(onClose = {}, viewModel = viewModel())
}

@Composable
@HorizontalScreenPreview
private fun AddPasswordScreenHorizontalPreview() {
    AddPasswordScreen(onClose = {}, viewModel = viewModel())
}