package com.example.passvault.utils.custom_composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.passvault.utils.annotations.VerticalScreenPreview

@Composable
fun TextFieldWithErrorText(
    label: String,
    value: String,
    onTextChange: (String) -> Unit,
    errorMsg: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onTextChange,
            label = { Text(text = label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMsg.isNotBlank(),
            singleLine = true,
        )
        if (errorMsg.isNotBlank()) ErrorText(errorText = errorMsg)
    }

}

@Composable
fun ErrorText(errorText: String) {
    Text(
        text = errorText,
        color = MaterialTheme.colorScheme.onErrorContainer,
        fontSize = 12.sp
    )

}

@Composable
fun ShowAndHidePasswordTextField(
    label: String,
    password: String,
    onTextChange: (String) -> Unit,
    showPassword: Boolean,
    onShowPasswordClick: () -> Unit,
    errorMsg: String,
    leadingIcon: ImageVector? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (leadingIcon == null) {
            OutlinedTextField(
                value = password,
                onValueChange = onTextChange,
                label = { Text(text = label) },
                visualTransformation =
                    if (showPassword) VisualTransformation.None
                    else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    if (showPassword) {
                        IconButton(onClick = onShowPasswordClick) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "show password"
                            )
                        }
                    } else {
                        IconButton(onClick = onShowPasswordClick) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "hide password"
                            )
                        }
                    }
                },
                isError = errorMsg.trim().isNotBlank(),
            )
        } else {
            OutlinedTextField(
                value = password,
                onValueChange = onTextChange,
                label = { Text(text = label) },
                visualTransformation =
                    if (showPassword) VisualTransformation.None
                    else NoPaddingPasswordTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                trailingIcon = {
                    if (showPassword) {
                        IconButton(onClick = onShowPasswordClick) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "show password"
                            )
                        }
                    } else {
                        IconButton(onClick = onShowPasswordClick) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "hide password"
                            )
                        }
                    }
                },
                isError = errorMsg.trim().isNotBlank(),
                leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = label) }
            )
        }
        if (errorMsg.isNotBlank()) ErrorText(errorText = errorMsg)
    }
}

@Composable
fun ConfirmationAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    vaultName: String,
    icon: ImageVector?
) {
    AlertDialog(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                icon?.let {
                    Icon(
                        icon,
                        contentDescription = "Confirmation icon",
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Text(
                    text = "Delete $vaultName",
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        text = {
            Text(
                text = buildAnnotatedString {
                    append("This will delete all passwords in the vault ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    ) { append("permanently") }
                    append(" . This action cannot be undone.\n\nAre you sure you want to proceed?")
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        onDismissRequest = { onDismissRequest() },
        dismissButton = { TextButton(onClick = { onDismissRequest() }) { Text("Dismiss") } },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(
                    "Confirm",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ConfirmationDialog(
    title: String,
    subTitle: String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = subTitle)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onDismissRequest, colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onConfirmation, colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}

@Composable
@VerticalScreenPreview
fun ConfirmationDialogPreview() {
    ConfirmationDialog(
        title = "Test",
        subTitle = "Some sub text specifying what can happen n ask for confirmation?",
        onDismissRequest = {}
    ) { }
}

@VerticalScreenPreview
@Composable
fun ConfirmationAlertDialogPreview() {
    ConfirmationAlertDialog(
        onDismissRequest = {},
        onConfirmation = {},
        vaultName = "Some Title",
        icon = Icons.Default.Home
    )
}

@Composable
fun TitleSquare(title: String) {
    val domainRegex = Regex("^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    val isDomain = domainRegex.matches(title.trim())

    Box(
        modifier = Modifier
            .size(55.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center
    ) {
        if (isDomain) {
            AsyncImage(
                model = "https://www.google.com/s2/favicons?sz=64&domain_url=https://$title",
                contentDescription = "Favicon",
                modifier = Modifier.size(30.dp)
            )
        } else {
            Text(
                text = title.take(2).uppercase(),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 22.sp,
                modifier = Modifier.padding(6.dp)
            )
        }
    }
}

@Composable
@VerticalScreenPreview
fun TitleSquarePreview() {
    TitleSquare(title = "Yuga")
}

class NoPaddingPasswordTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformed = AnnotatedString("•".repeat(text.length))
        return TransformedText(transformed, OffsetMapping.Identity)
    }
}
