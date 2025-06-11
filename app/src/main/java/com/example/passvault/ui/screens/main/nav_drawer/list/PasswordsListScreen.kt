package com.example.passvault.ui.screens.main.nav_drawer.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.passvault.data.PasswordDetailResult
import com.example.passvault.data.PasswordDetails
import com.example.passvault.utils.annotations.HorizontalScreenPreview
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.example.passvault.utils.custom_composables.TitleSquare
import kotlinx.coroutines.launch

@Composable
fun PasswordsListScreen(
    passwordDetailResultList: List<PasswordDetailResult>,
    onAddClick: () -> Unit,
    toViewScreen: (passwordDetailResult: PasswordDetailResult) -> Unit,
    toEditScreen: (passwordDetailResult: PasswordDetailResult) -> Unit,
    onDeleteClick: (passwordId: Long?) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {
        if (passwordDetailResultList.isEmpty()) {
            // TODO: create ui to display empty list 
            Text(text = "No passwords stored in this Vault!")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(passwordDetailResultList) { passwordDetailResult ->
                    PasswordItem(
                        passwordDetails = passwordDetailResult.passwordDetails,
                        onViewClick = { toViewScreen(passwordDetailResult) },
                        onEditClick = { toEditScreen(passwordDetailResult) },
                        onDeleteClick = { onDeleteClick(passwordDetailResult.passwordId) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordItem(
    passwordDetails: PasswordDetails,
    onViewClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    if (sheetState.isVisible) {
        ModalBottomSheet(onDismissRequest = { scope.launch { sheetState.hide() } }) {
            MoreOptionsBottomSheetContent(
                passwordDetails = passwordDetails,
                onViewClick = {
                    scope.launch { sheetState.hide() }
                    onViewClick()
                },
                onEditClick = {
                    scope.launch { sheetState.hide() }
                    onEditClick()
                },
                onDeleteClick = {
                    scope.launch { sheetState.hide() }
                    onDeleteClick()
                },
            )
        }
    }
    Card(
        onClick = { onViewClick() },
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
            IconButton(onClick = { scope.launch { sheetState.show() } }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More"
                )
            }
        }
    }
}

@Composable
fun MoreOptionsBottomSheetContent(
    passwordDetails: PasswordDetails,
    onViewClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val items = listOf(
        MoreOption(
            title = "View",
            imageVector = Icons.Outlined.Visibility,
            onClick = onViewClick
        ),
        MoreOption(
            title = "Edit",
            imageVector = Icons.Outlined.Edit,
            onClick = onEditClick
        ),
        MoreOption(
            title = "Delete",
            imageVector = Icons.Outlined.Delete,
            onClick = onDeleteClick
        )
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append(text = passwordDetails.title) }
            append(text = "\n")
            if (passwordDetails.email.isNotBlank()) append(text = passwordDetails.email)
            else if (passwordDetails.website.isNotBlank()) append(text = passwordDetails.website)
        })
        Spacer(modifier = Modifier.size(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 12.dp)
        ) {
            items(items = items) { item ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        onClick = item.onClick,
                        shape = CircleShape,
                        colors = if (item.title == "Delete") {
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        } else {
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        modifier = Modifier.size(65.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.imageVector,
                                contentDescription = "Button for ${item.title}"
                            )
                        }
                    }
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (item.title == "Delete") {
                                MaterialTheme.colorScheme.onErrorContainer
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            }
                        )
                    )
                }
            }
        }

    }
}

data class MoreOption(
    val title: String,
    val imageVector: ImageVector,
    val onClick: () -> Unit
)

@Composable
@VerticalScreenPreview
fun MoreOptionsBottomSheetPreview() {
    MoreOptionsBottomSheetContent(
        passwordDetails = PasswordDetails.mockPasswordDetails,
        onViewClick = {},
        onEditClick = {}
    ) { }
}

@Composable
@VerticalScreenPreview
fun PasswordItemVertical() {
    PasswordItem(
        onViewClick = {},
        onEditClick = {},
        passwordDetails = PasswordDetails.mockPasswordDetails,
        onDeleteClick = {},
    )
}

@Composable
@HorizontalScreenPreview
fun PasswordItemHorizontal() {
    PasswordItem(
        onViewClick = {},
        onEditClick = {},
        passwordDetails = PasswordDetails.mockPasswordDetails,
        onDeleteClick = {},
    )
}

@Composable
@VerticalScreenPreview
fun PasswordListScreenVertical() {
    PasswordsListScreen(
        onAddClick = {},
        passwordDetailResultList = List(10) { PasswordDetailResult.mockObject },
        toViewScreen = {},
        toEditScreen = {},
        onDeleteClick = {},
    )
}

@Composable
@HorizontalScreenPreview
fun PasswordListScreenHorizontal() {
    PasswordsListScreen(
        onAddClick = {},
        passwordDetailResultList = List(10) { PasswordDetailResult.mockObject },
        toViewScreen = {},
        toEditScreen = {},
        onDeleteClick = {},
    )
}