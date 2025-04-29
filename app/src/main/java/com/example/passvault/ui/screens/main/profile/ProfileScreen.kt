package com.example.passvault.ui.screens.main.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen(
) {
    Column (modifier = Modifier.fillMaxSize()){
        Text("Profile..")
        Button(onClick = {}) {
            Text("Logout")
        }
    }
}