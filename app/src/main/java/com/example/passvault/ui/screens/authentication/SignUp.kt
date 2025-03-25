package com.example.passvault.ui.screens.authentication

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passvault.R
import com.example.passvault.ui.screens.state.ScreenState

@Composable
fun SignUp(
    navToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var showConfirmPassword by rememberSaveable { mutableStateOf(false) }
    val currentContext = LocalContext.current
    val screenState by authViewModel.screenState.collectAsState()

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
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        ShowAndHidePasswordTextField(
            label = "Password",
            password = password,
            onTextChange = { password = it },
            showPassword = showPassword,
            onShowPasswordClick = { showPassword = !showPassword }
        )
        ShowAndHidePasswordTextField(
            label = "Confirm Password",
            password = confirmPassword,
            onTextChange = { confirmPassword = it },
            showPassword = showConfirmPassword,
            onShowPasswordClick = { showConfirmPassword = !showConfirmPassword }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                if (email.trim().isNotBlank()
                    and password.trim().isNotBlank()
                    and confirmPassword.trim().isNotBlank()
                    and (password.trim() == confirmPassword.trim()
                            )
                ) {
                    authViewModel.signUpWithEmail(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp)
        ) {
            if (screenState is ScreenState.Loading) {
                Text("Loading..")
            } else {
                Text("Create account")

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
        when (screenState) {
            is ScreenState.Loaded -> {
                Toast.makeText(
                    currentContext, (screenState as ScreenState.Loaded<String>).result,
                    Toast.LENGTH_SHORT
                ).show()
                navToLogin()
            }

            is ScreenState.Error -> {
                Toast.makeText(
                    currentContext,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
                authViewModel.setScreenStateToPreLoad()
            }

            else -> {}
        }
    }
}

@Composable
fun ShowAndHidePasswordTextField(
    label: String,
    password: String,
    onTextChange: (String) -> Unit,
    showPassword: Boolean,
    onShowPasswordClick: () -> Unit
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
        }
    )
}

@Composable
@Preview
fun SignUpPreview() {
    Surface {
        SignUp(
            navToLogin = {})
    }
}