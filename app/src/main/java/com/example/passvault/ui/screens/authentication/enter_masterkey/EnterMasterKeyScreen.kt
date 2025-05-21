package com.example.passvault.ui.screens.authentication.enter_masterkey

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.passvault.R
import com.example.passvault.di.shared_reference.EncryptedPrefsModule
import com.example.passvault.di.shared_reference.MasterCredentialsRepository
import com.example.passvault.di.supabase.SupabaseModule
import com.example.passvault.network.supabase.UserRepository
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.example.passvault.utils.custom_composables.ShowAndHidePasswordTextField
import com.example.passvault.utils.state.ScreenState

@Composable
fun EnterMasterKeyScreen(
    viewModel: EnterMasterKeyViewModel,
    onUnlocked: () -> Unit
) {
    val screenState by viewModel.screenState.collectAsState()
    val currentContext = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.large_padding))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Unlock your vault",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(dimensionResource(R.dimen.small_padding))
        )
        Text(
            text = "Enter your Master Key to unlock your passwords",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.large_padding))
        )
        ShowAndHidePasswordTextField(
            label = "Master Key",
            password = viewModel.masterKey,
            onTextChange = { viewModel.onMasterKeyChange(it) },
            showPassword = viewModel.showMasterKeyPassword,
            onShowPasswordClick = { viewModel.toggleMasterKeyVisibility() },
            errorMsg = viewModel.masterKeyError,
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_spacer_height)))
        Button(
            onClick = {
                viewModel.submitMasterKey()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.min_clickable_size)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_radius)),
            enabled = screenState !is ScreenState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = when (screenState) {
                    is ScreenState.Loading -> "Loading.."
                    else -> "Unlock"
                }
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "*Note: This key is unrecoverable if lost or forgotten",
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
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
                        Toast.LENGTH_SHORT
                    ).show()
                    onUnlocked()
                }

                else -> {}
            }
        }
    }
}

@Composable
@VerticalScreenPreview
private fun EnterMasterKeyScreenPreview() {
    EnterMasterKeyScreen(
        viewModel = EnterMasterKeyViewModel(
            userRepository = UserRepository(
                supabaseClient = SupabaseModule.mockClient
            ),
            masterCredentialsRepository = MasterCredentialsRepository(
                encryptedPrefs = EncryptedPrefsModule.mockSharedPreference
            )
        ),
        onUnlocked = {}
    )
}

@Composable
@HorizontalScreenPreview
private fun EnterMasterKeyScreenHorizontalPreview() {
    EnterMasterKeyScreen(
        viewModel = EnterMasterKeyViewModel(
            userRepository = UserRepository(
                supabaseClient = SupabaseModule.mockClient
            ),
            masterCredentialsRepository = MasterCredentialsRepository(
                encryptedPrefs = EncryptedPrefsModule.mockSharedPreference
            )
        ),
        onUnlocked = {}
    )
}