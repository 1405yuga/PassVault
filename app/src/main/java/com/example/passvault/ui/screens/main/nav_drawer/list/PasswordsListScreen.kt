package com.example.passvault.ui.screens.main.nav_drawer.list

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.passvault.data.PasswordDetailsWithId
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.example.passvault.utils.custom_composables.TitleSquare

@Composable
fun PasswordsListScreen(
    passwordDetailsWithIdList: List<PasswordDetailsWithId>,
    onAddClick: () -> Unit,
    onItemClick: (passWordId: Long?) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (passwordDetailsWithIdList.isEmpty()) {
            // TODO: create ui to display empty list 
            Text(text = "No passwords stored in this Vault!")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(passwordDetailsWithIdList) { passwordDetail ->
                    PasswordItem(
                        passwordDetailsWithId = passwordDetail,
                        onItemClick = { onItemClick(it) },
                        onMoreClick = {
                            // TODO: open bottom modal
                        }
                    )
                }
            }
        }
        ExtendedFloatingActionButton(
            onClick = onAddClick,
            text = { Text(text = "Add Password") },
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .defaultMinSize(48.dp)
                .padding(bottom = 22.dp, end = 8.dp)
        )
    }
}

@Composable
fun PasswordItem(
    passwordDetailsWithId: PasswordDetailsWithId,
    onItemClick: (passWordId: Long?) -> Unit,
    onMoreClick: () -> Unit
) {
    val passwordDetails = passwordDetailsWithId.passwordDetails
    Card(
        onClick = { onItemClick(passwordDetailsWithId.passwordId) },
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
            IconButton(onClick = { onMoreClick() }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More"
                )
            }
        }
    }
}

@Composable
@VerticalScreenPreview
fun PasswordItemVertical() {
    PasswordItem(
        passwordDetailsWithId = PasswordDetailsWithId.mockObject,
        onItemClick = {},
        onMoreClick = {},
    )
}

@Composable
@HorizontalScreenPreview
fun PasswordItemHorizontal() {
    PasswordItem(
        passwordDetailsWithId = PasswordDetailsWithId.mockObject,
        onItemClick = {},
        onMoreClick = {},
    )
}

@Composable
@VerticalScreenPreview
fun PasswordListScreenVertical() {
    PasswordsListScreen(
        onAddClick = {},
        passwordDetailsWithIdList = List(10) { PasswordDetailsWithId.mockObject },
        onItemClick = {}
    )
}

@Composable
@HorizontalScreenPreview
fun PasswordListScreenHorizontal() {
    PasswordsListScreen(
        onAddClick = {},
        passwordDetailsWithIdList = List(10) { PasswordDetailsWithId.mockObject },
        onItemClick = {}
    )
}