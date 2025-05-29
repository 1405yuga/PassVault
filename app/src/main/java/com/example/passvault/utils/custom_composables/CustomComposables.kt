package com.example.passvault.utils.custom_composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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

@VerticalScreenPreview
@Composable
fun ConfirmationDialogPreview() {
    ConfirmationAlertDialog(
        onDismissRequest = {},
        onConfirmation = {},
        vaultName = "Some Title",
        icon = Icons.Default.Home
    )
}

@Composable
fun TitleSquare(title: String) {
    Box(
        modifier = Modifier
            .size(55.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.onSecondary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title.take(2).uppercase(),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 22.sp,
            modifier = Modifier.padding(6.dp)
        )
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
