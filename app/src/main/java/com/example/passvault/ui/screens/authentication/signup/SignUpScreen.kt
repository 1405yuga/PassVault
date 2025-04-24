package com.example.passvault.ui.screens.authentication.signup

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.example.passvault.R
import com.example.passvault.di.supabase.SupabaseModule
import com.example.passvault.network.supabase.AuthRepository
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.example.passvault.utils.state.ScreenState

@Composable
fun SignUpScreen(
    navToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel
) {
    val currentContext = LocalContext.current
    val screenState by viewModel.screenState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(R.dimen.large_padding))
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create account",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(dimensionResource(R.dimen.small_padding))
        )
        Text(
            text = "Join to protect your passwords securely",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.large_padding))
        )
        TextFieldWithErrorText(
            label = "Email",
            value = viewModel.email,
            onTextChange = { viewModel.onEmailChange(it) },
            errorMsg = viewModel.emailError
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_spacer_height)))
        ShowAndHidePasswordTextField(
            label = "Password",
            password = viewModel.password,
            onTextChange = { viewModel.onPasswordChange(it) },
            showPassword = viewModel.showPassword,
            onShowPasswordClick = { viewModel.togglePasswordVisibility() },
            errorMsg = viewModel.passwordError
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_spacer_height)))
        ShowAndHidePasswordTextField(
            label = "Confirm Password",
            password = viewModel.confirmPassword,
            onTextChange = { viewModel.onConfirmPasswordChange(it) },
            showPassword = viewModel.showConfirmPassword,
            onShowPasswordClick = { viewModel.toggleConfirmPasswordVisibility() },
            errorMsg = viewModel.confirmPasswordError
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_spacer_height)))
        Button(
            onClick = {
                viewModel.emailSignUp(
                    email = viewModel.email,
                    password = viewModel.password
                )
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
                    else -> "Get started"
                }
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_spacer_height)))
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "  or  ",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_spacer_height)))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Already have an account? ")
            Text(
                text = "Log in",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.clickable { navToLogin() }
            )
        }
        LaunchedEffect(screenState) {
            when (val state = screenState) {
                is ScreenState.Loaded -> {
                    Toast.makeText(
                        currentContext,
                        state.result,
                        Toast.LENGTH_LONG
                    ).show()
                    navToLogin()
                }

                is ScreenState.Error -> Toast.makeText(
                    currentContext,
                    state.message,
                    Toast.LENGTH_LONG
                ).show()

                else -> {}
            }
        }
    }
}

@Composable
fun TextFieldWithErrorText(
    label: String,
    value: String,
    onTextChange: (String) -> Unit,
    errorMsg: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onTextChange,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier.fillMaxWidth(),
        isError = errorMsg.isNotBlank(),
        singleLine = true,
        supportingText = {
            if (errorMsg.isNotBlank()) {
                Text(
                    text = errorMsg,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontSize = 12.sp
                )
            }
        }
    )
}

@Composable
fun ShowAndHidePasswordTextField(
    label: String,
    password: String,
    onTextChange: (String) -> Unit,
    showPassword: Boolean,
    onShowPasswordClick: () -> Unit,
    errorMsg: String
) {
    OutlinedTextField(
        value = password,
        onValueChange = onTextChange,
        label = { Text(text = label) },
        visualTransformation =
            if (showPassword) VisualTransformation.None
            else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        trailingIcon = {
            if (showPassword) {
                IconButton(onClick = onShowPasswordClick) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = "show password"
                    )
                }
            } else {
                IconButton(onClick = onShowPasswordClick) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = "hide password"
                    )
                }
            }
        },
        isError = errorMsg.trim().isNotBlank(),
        supportingText = {
            if (errorMsg.isNotBlank()) {
                Text(
                    text = errorMsg,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontSize = 12.sp
                )
            }
        }
    )
}

@Composable
@VerticalScreenPreview
private fun SignUpScreenPreview() {
    SignUpScreen(
        navToLogin = {},
        viewModel = SignUpViewModel(
            authRepository = AuthRepository(
                supabaseClient = SupabaseModule.mockClient
            )
        ),
    )
}

@Composable
@HorizontalScreenPreview
private fun SignUpScreenHorizontalPreview() {
    SignUpScreen(
        navToLogin = {},
        viewModel = SignUpViewModel(
            authRepository = AuthRepository(
                supabaseClient = SupabaseModule.mockClient
            )
        ),
    )
}