package com.example.passvault.ui.screens.main.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.passvault.data.PasswordDetails
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.example.passvault.utils.custom_composables.TitleSquare

@Composable
fun PasswordsListScreen(
    passwordDetailsList: List<PasswordDetails>,
    onAddClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (passwordDetailsList.isEmpty()) {
            // TODO: create ui to display empty list 
            Text(text = "No passwords stored in this Vault!")
        } else {
            // TODO: create ui to display list
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(passwordDetailsList) { passwordDetail ->
                    PasswordItem(
                        passwordDetails = passwordDetail,
                        onItemClick = {
                            // TODO: go to screen where all details are shown
                        },
                        onMoreClick = {
                            // TODO: open bottom modal
                        }
                    )
                }
            }
        }
        Button(onClick = onAddClick) {
            Text("Go to add screen")
        }
    }
}

@Composable
fun PasswordItem(
    passwordDetails: PasswordDetails,
    onItemClick: (passwordDetails: PasswordDetails) -> Unit,
    onMoreClick: () -> Unit
) {
    Card(
        onClick = { onItemClick(passwordDetails) },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 6.dp)
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            TitleSquare(title = passwordDetails.title)
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ) { append(passwordDetails.title) }
                    if (passwordDetails.email.isNotEmpty()) {
                        append("\n${passwordDetails.email}")
                    }
                }, modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(onClick = {
                onMoreClick()
            }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
            }
        }
    }
}

@Composable
@VerticalScreenPreview
fun PasswordItemVertical() {
    PasswordItem(
        passwordDetails = PasswordDetails.mockPasswordDetails,
        onItemClick = {},
        onMoreClick = {},
    )
}

@Composable
@HorizontalScreenPreview
fun PasswordItemHorizontal() {
    PasswordItem(
        passwordDetails = PasswordDetails.mockPasswordDetails,
        onItemClick = {},
        onMoreClick = {},
    )
}

@Composable
@VerticalScreenPreview
fun PasswordListScreenVertical() {
    PasswordsListScreen(
        onAddClick = {},
        passwordDetailsList = List(10) { PasswordDetails.mockPasswordDetails }
    )
}

@Composable
@HorizontalScreenPreview
fun PasswordListScreenHorizontal() {
    PasswordsListScreen(
        onAddClick = {},
        passwordDetailsList = List(10) { PasswordDetails.mockPasswordDetails }
    )
}