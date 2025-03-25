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
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passvault.R
import com.example.passvault.ui.screens.state.ScreenState

@Composable
fun Login(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
) {
    var email by rememberSaveable { mutableStateOf("") }
    var emailError by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordError by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    val screenState by authViewModel.screenState.collectAsState()
    val currentContext = LocalContext.current

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
                    authViewModel.loginWithEmail(email.trim(), password.trim())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp)
        ) {
            if (screenState is ScreenState.Loading) {
                Text("Loading..")
            } else {
                Text("Get started")

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
        when (screenState) {
            is ScreenState.Loaded -> {
                Toast.makeText(
                    currentContext, (screenState as ScreenState.Loaded<String>).result,
                    Toast.LENGTH_SHORT
                ).show()
                onLoginClick()
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
@Preview
fun LoginPreview() {
    Surface {
        Login(onLoginClick = {}, onSignUpClick = {})
    }
}