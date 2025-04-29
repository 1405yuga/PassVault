package com.example.passvault.ui.screens.main.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PasswordsListScreen(onAddClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = onAddClick) {
            Text("Go to add screen")
        }
    }
}