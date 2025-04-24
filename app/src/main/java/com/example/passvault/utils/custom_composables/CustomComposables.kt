package com.example.passvault.utils.custom_composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp

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

class NoPaddingPasswordTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformed = AnnotatedString("•".repeat(text.length))
        return TransformedText(transformed, OffsetMapping.Identity)
    }
}
