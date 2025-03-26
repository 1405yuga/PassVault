package com.example.passvault.ui.screens.main_screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Profile(
    onLogoutClick: () -> Unit
) {
    Text("Profile..")
    Button(onClick = onLogoutClick) {
        Text("Logout")
    }
}