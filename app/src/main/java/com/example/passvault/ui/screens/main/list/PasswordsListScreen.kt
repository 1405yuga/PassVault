package com.example.passvault.ui.screens.main.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.passvault.data.EncryptedData
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview

@Composable
fun PasswordsListScreen(
    encryptedDataList: List<EncryptedData>,
    onAddClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Passwords : ${encryptedDataList.size}")
        Button(onClick = onAddClick) {
            Text("Go to add screen")
        }
    }
}

@Composable
@VerticalScreenPreview
fun PasswordListScreenVertical() {
    PasswordsListScreen(
        onAddClick = {},
        encryptedDataList = emptyList()
    )
}

@Composable
@HorizontalScreenPreview
fun PasswordListScreenHorizontal() {
    PasswordsListScreen(
        onAddClick = {},
        encryptedDataList = emptyList()
    )
}