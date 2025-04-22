package com.example.passvault.ui.screens.authentication.create_masterkey

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passvault.di.supabase.SupabaseModule
import com.example.passvault.network.supabase.UserRepository
import com.example.passvault.ui.screens.authentication.signup.ShowAndHidePasswordTextField
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.example.passvault.utils.state.ScreenState

@Composable
fun CreateMasterKeyScreen(
    onConfirmClick: () -> Unit,
    viewModel: CreateMasterKeyViewModel
) {
    val screenState by viewModel.screenState.collectAsState()
    val currentContext = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Master Key",
            fontSize = 22.sp
        )
        Text(text = "Create master key for encryption")
        ShowAndHidePasswordTextField(
            label = "Master Key",
            password = viewModel.masterKey,
            onTextChange = { viewModel.onMasterKeyChange(it) },
            showPassword = viewModel.showMasterKeyPassword,
            onShowPasswordClick = { viewModel.toggleMasterKeyVisibility() },
            errorMsg = viewModel.masterKeyError,
        )

        ShowAndHidePasswordTextField(
            label = "Confirm Master Key",
            password = viewModel.confirmedMasterKey,
            onTextChange = { viewModel.onConfirmedMasterKeyChange(it) },
            showPassword = viewModel.showConfirmedMasterKeyPassword,
            onShowPasswordClick = { viewModel.toggleConfirmedMasterKeyVisibility() },
            errorMsg = viewModel.confirmedMasterKeyError,
        )
        Button(
            onClick = {
                viewModel.insertMasterKey()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            enabled = screenState !is ScreenState.Loading,
        ) {
            Text(
                text = when (screenState) {
                    is ScreenState.Loading -> "Loading.."
                    else -> "Confirm"
                }
            )
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
@VerticalScreenPreview
private fun CreateMasterKeyScreenPreview() {
    CreateMasterKeyScreen(
        onConfirmClick = {},
        viewModel = CreateMasterKeyViewModel(
            userRepository = UserRepository(
                supabaseClient = SupabaseModule.mockClient
            )
        ),
    )
}

@Composable
@HorizontalScreenPreview
private fun CreateMasterKeyScreenHorizontalPreview() {
    CreateMasterKeyScreen(
        onConfirmClick = {},
        viewModel = CreateMasterKeyViewModel(
            userRepository = UserRepository(
                supabaseClient = SupabaseModule.mockClient
            )
        ),
    )
}