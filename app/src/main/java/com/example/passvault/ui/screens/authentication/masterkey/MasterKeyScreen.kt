package com.example.passvault.ui.screens.authentication.masterkey

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passvault.ui.screens.authentication.signup.ShowAndHidePasswordTextField

@Composable
fun MasterKeyScreen() {
    var password by rememberSaveable { mutableStateOf("") }
    val passwordError by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
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
            password = password,
            onTextChange = { password = it },
            showPassword = showPassword,
            onShowPasswordClick = { showPassword = !showPassword },
            errorMsg = passwordError,
        )
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp)
        ) { Text(text = "Confirm") }
        Text(
            text = "*Note: This key is unrecoverable if lost or forgotten",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}

@Preview
@Composable
fun MasterKeyScreenPreview() {
    Surface {
        MasterKeyScreen()
    }
}