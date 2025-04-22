package com.example.passvault.ui.screens.authentication.enter_masterkey

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
import com.example.passvault.di.supabase.SupabaseModule
import com.example.passvault.network.supabase.AuthRepository
import com.example.passvault.network.supabase.UserRepository
import com.example.passvault.ui.screens.authentication.signup.ShowAndHidePasswordTextField
import com.example.passvault.ui.state.ScreenState

@Composable
fun EnterMasterKeyScreen(
    viewModel: EnterMasterKeyViewModel
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
        Text(text = "Enter the master key")
        ShowAndHidePasswordTextField(
            label = "Master Key",
            password = uiState.masterKey,
            onTextChange = { viewModel.onMasterKeyChange(it) },
            showPassword = uiState.showMasterKeyPassword,
            onShowPasswordClick = { viewModel.toggleMasterKeyVisibility() },
            errorMsg = uiState.masterKeyError,
        )
        Button(
            onClick = {
                viewModel.submitMasterKey()
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
//                    onConfirmClick()
                }

                else -> {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EnterMasterKeyScreenPreview() {
        EnterMasterKeyScreen(
            viewModel = EnterMasterKeyViewModel(
                userRepository = UserRepository(
                    supabaseClient = SupabaseModule.mockClient
                ),
                authRepository = AuthRepository(
                    supabaseClient = SupabaseModule.mockClient
                )
            )
        )
}