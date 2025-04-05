package com.example.passvault.ui.screens.login

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.passvault.ui.screens.signup.ShowAndHidePasswordTextField
import com.example.passvault.ui.screens.signup.TextFieldWithErrorText
import com.example.passvault.ui.screens.signup.validateEmail
import com.example.passvault.ui.screens.signup.validatePassword
import com.example.passvault.ui.screens.state.ScreenState

@Composable
fun Login(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordError by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
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
            value = email,
            onTextChange = { email = it },
            errorMsg = emailError
        )
        ShowAndHidePasswordTextField(
            label = "Password",
            password = password,
            onTextChange = { password = it },
            showPassword = showPassword,
            onShowPasswordClick = { showPassword = !showPassword },
            errorMsg = passwordError
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                validateEmail(email = email, setErrorMsg = { emailError = it })
                validatePassword(password = password, setPasswordError = { passwordError = it })
                if (emailError.isBlank() and passwordError.isBlank()) {
                    viewModel.emailLogin(
                        email = email.trim(),
                        password = password.trim()
                    )
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
        Login(onLoginClick = {}, onSignUpClick = {})
    }
}