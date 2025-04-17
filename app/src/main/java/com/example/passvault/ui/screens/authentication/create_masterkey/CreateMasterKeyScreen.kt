package com.example.passvault.ui.screens.authentication.create_masterkey

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passvault.ui.screens.authentication.signup.ShowAndHidePasswordTextField
import com.example.passvault.ui.state.ScreenState

@Composable
fun CreateMasterKeyScreen(
    onConfirmClick: () -> Unit,
    viewModel: CreateMasterKeyViewModel
) {
    val uiState = viewModel.uiState
    val screenState by viewModel.screenState.collectAsState()
    val currentContext = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Master Key",
            fontSize = 22.sp
        )
        Text(text = "Create master key for encryption")
        ShowAndHidePasswordTextField(
            label = "Master Key",
            password = uiState.masterKey,
            onTextChange = { viewModel.onMasterKeyChange(it) },
            showPassword = uiState.showMasterKeyPassword,
            onShowPasswordClick = { viewModel.toggleMasterKeyVisibility() },
            errorMsg = uiState.masterKeyError,
        )

        ShowAndHidePasswordTextField(
            label = "Confirm Master Key",
            password = uiState.confirmedMasterKey,
            onTextChange = { viewModel.onConfirmedMasterKeyChange(it) },
            showPassword = uiState.showConfirmedMasterKeyPassword,
            onShowPasswordClick = { viewModel.toggleConfirmedMasterKeyVisibility() },
            errorMsg = uiState.confirmedMasterKeyError,
        )
        Button(
            onClick = {
                viewModel.insertMasterKey()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            enabled = screenState !is ScreenState.Loading
        ) {
            when (screenState) {
                is ScreenState.Loading -> Text("Loading..")
                else -> Text("Confirm")
            }
        }
        Text(
            text = "*Note: This key is unrecoverable if lost or forgotten",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
    LaunchedEffect(screenState) {
        when (val state = screenState) {
            is ScreenState.Error -> Toast.makeText(
                currentContext,
                state.message,
                Toast.LENGTH_LONG
            ).show()

            is ScreenState.Loaded -> {
                Toast.makeText(
                    currentContext,
                    state.result,
                    Toast.LENGTH_LONG
                ).show()
                onConfirmClick()
            }

            else -> {}
        }
    }
}

@Composable
@Preview()
fun MasterKeyCreationCreationScreenPreview() {
    Surface {
        CreateMasterKeyScreen(
            onConfirmClick = {},
            viewModel()
        )
    }
}