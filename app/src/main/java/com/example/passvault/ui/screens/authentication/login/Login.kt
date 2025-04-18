package com.example.passvault.ui.screens.authentication.login

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
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passvault.di.supabase.SupabaseModule
import com.example.passvault.network.supabase.AuthRepository
import com.example.passvault.ui.screens.authentication.signup.ShowAndHidePasswordTextField
import com.example.passvault.ui.screens.authentication.signup.TextFieldWithErrorText
import com.example.passvault.ui.state.ScreenState

@Composable
fun Login(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel
) {
    val uiState = viewModel.uiState
    val screenState by viewModel.screenState.collectAsState()
    val currentContext = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome Back!",
            fontSize = 22.sp
        )
        Text(text = "Please enter login details to get started!")
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
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                viewModel.emailLogin()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            enabled = screenState !is ScreenState.Loading
        ) {
            when (screenState) {
                is ScreenState.Loading -> Text("Loading..")
                else -> Text("Get started")
            }
        }
        Spacer(modifier = Modifier.height(22.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Don't have account? ")
            Text(
                text = "Create new account",
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.clickable {
                    onSignUpClick()
                }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "or",
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        LaunchedEffect(screenState) {
            when (val state = screenState) {
                is ScreenState.Error -> Toast.makeText(
                    currentContext,
                    state.message,
                    Toast.LENGTH_LONG
                ).show()

                is ScreenState.Loaded -> {
                    onLoginClick()
                }

                else -> {}
            }
        }

//        Button(onClick = {
//            // TODO: google login
//        }) {
//            Icon(
//                painter = painterResource(R.drawable.google_icon),
//                contentDescription = null
//            )
//            Spacer(modifier = Modifier.)
//            Text(text = "Log in with Google")
//        }
//        when (screenState) {
//            is ScreenState.Loaded -> {
//                Toast.makeText(
//                    currentContext, (screenState as ScreenState.Loaded<String>).result,
//                    Toast.LENGTH_SHORT
//                ).show()
//                authViewModel.setScreenStateToPreLoad()
//                onLoginClick()
//            }
//
//            is ScreenState.Error -> {
//                Toast.makeText(
//                    currentContext,
//                    "Something went wrong",
//                    Toast.LENGTH_SHORT
//                ).show()
//                authViewModel.setScreenStateToPreLoad()
//            }
//
//            else -> {}
//        }
    }
}

@Composable
@Preview
fun LoginPreview() {
    Surface {
        Login(
            onLoginClick = {}, onSignUpClick = {}, viewModel = LoginViewModel(
                AuthRepository(
                    SupabaseModule.mockClient
                )
            )
        )
    }
}