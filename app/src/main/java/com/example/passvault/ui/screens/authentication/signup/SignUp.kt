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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.passvault.R
import com.example.passvault.ui.state.ScreenState

@Composable
fun SignUp(
    navToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val currentContext = LocalContext.current
    val screenState by viewModel.screenState.collectAsState()
    val uiState = viewModel.uiState
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 22.sp
        )
        Text(text = "Please enter details to create an account!")
        Spacer(modifier = Modifier.height(20.dp))
        TextFieldWithErrorText(
            label = "Email",
            value = uiState.email,
            onTextChange = { viewModel.onEmailChange(it) },
            errorMsg = uiState.emailError
        )
        ShowAndHidePasswordTextField(
            label = "Password",
            password = uiState.password,
            onTextChange = { viewModel.onPasswordChange(it) },
            showPassword = uiState.showPassword,
            onShowPasswordClick = { viewModel.togglePasswordVisibility() },
            errorMsg = uiState.passwordError
        )
        ShowAndHidePasswordTextField(
            label = "Confirm Password",
            password = uiState.confirmPassword,
            onTextChange = { viewModel.onConfirmPasswordChange(it) },
            showPassword = uiState.showConfirmPassword,
            onShowPasswordClick = { viewModel.toggleConfirmPasswordVisibility() },
            errorMsg = uiState.confirmPasswordError
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                validateEmail(email = uiState.email, setErrorMsg = { viewModel.setEmailError(it) })
                validatePassword(
                    password = uiState.password,
                    setPasswordError = { viewModel.setPasswordError(it) })
                validatePassword(
                    password = uiState.confirmPassword,
                    setPasswordError = { viewModel.setConfirmPasswordError(it) })
                when {
                    uiState.password.trim() != uiState.confirmPassword.trim()
                        -> viewModel.setConfirmPasswordError("Passwords don't match!")

                    uiState.emailError.isBlank() and uiState.passwordError.isBlank() and uiState.confirmPasswordError.isBlank()
                        -> viewModel.emailSignUp(uiState.email, uiState.password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            enabled = screenState !is ScreenState.Loading
        ) {
            when (screenState) {
                is ScreenState.Loading -> {
                    Text("Loading..")
                }

                else -> {
                    Text("Get started")
                }
            }
        }
        Spacer(modifier = Modifier.height(22.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already have an account? ")
            Text(
                text = "Log in",
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.clickable {
                    navToLogin()
                }
            )
        }
        LaunchedEffect(screenState) {
            when (val state = screenState) {
                is ScreenState.Loaded -> Toast.makeText(
                    currentContext,
                    state.result,
                    Toast.LENGTH_LONG
                ).show()

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
fun ErrorText(errorMessage: String) {
    Text(
        errorMessage, color = MaterialTheme.colorScheme.onErrorContainer,
        fontSize = 12.sp
    )
}

fun validateEmail(email: String, setErrorMsg: (String) -> Unit) {
    setErrorMsg("")
    when {
        email.trim().isBlank() -> setErrorMsg("Email cannot be empty")
        //todo:validate email
    }
}

fun validatePassword(password: String, setPasswordError: (String) -> Unit) {
    setPasswordError("")
    when {
        password.trim().isBlank() -> setPasswordError("Password cannot be empty")
        password.trim().length < 6 -> setPasswordError(
            "Password must be at least of 6 characters!"
        )
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier.fillMaxWidth(),
        isError = errorMsg.isNotBlank(),
    )
    ErrorText(errorMessage = errorMsg)
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
        isError = errorMsg.trim().isNotBlank()
    )
    ErrorText(errorMessage = errorMsg)
}

@Composable
@Preview
fun SignUpPreview() {
    Surface {
        SignUp(
            navToLogin = {}
        )
    }
}