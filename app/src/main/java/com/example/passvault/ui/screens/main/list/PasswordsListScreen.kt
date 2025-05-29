package com.example.passvault.ui.screens.main.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.passvault.data.PasswordDetails
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview

@Composable
fun PasswordsListScreen(
    passwordDetailsList: List<PasswordDetails>,
    onAddClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (passwordDetailsList.isEmpty()) {
            // TODO: create ui to display empty list 
            Text(text = "No passwords stored in this Vault!")
        } else {
            // TODO: create ui to display list
            Text(text = "Passwords : ${passwordDetailsList.size}")
        }
        Button(onClick = onAddClick) {
            Text("Go to add screen")
        }
    }
}

@Composable
fun PasswordItem() {
}

@Composable
@VerticalScreenPreview
fun PasswordListScreenVertical() {
    PasswordsListScreen(
        onAddClick = {},
        passwordDetailsList = emptyList()
    )
}

@Composable
@HorizontalScreenPreview
fun PasswordListScreenHorizontal() {
    PasswordsListScreen(
        onAddClick = {},
        passwordDetailsList = emptyList()
    )
}