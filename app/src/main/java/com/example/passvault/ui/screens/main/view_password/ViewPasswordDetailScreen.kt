package com.example.passvault.ui.screens.main.view_password

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.passvault.R
import com.example.passvault.data.PasswordDetailResult
import com.example.passvault.data.Vault
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.example.passvault.utils.custom_composables.ConfirmationDialog
import com.example.passvault.utils.custom_composables.TitleSquare
import com.example.passvault.utils.extension_functions.toImageVector
import com.example.passvault.utils.helper.DateTimeHelper
import com.example.passvault.utils.state.ScreenState

@Composable
fun ViewPasswordDetailScreen(
    passwordDetailsResult: PasswordDetailResult,
    viewModel: ViewPasswordDetailViewModel,
    toEditPasswordScreen: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
//    val screenState by viewModel.screenState.collectAsState()

    val deletePasswordScreenState by viewModel.deletePasswordScreenState.collectAsState()
    val currentContext = LocalContext.current

    LaunchedEffect(deletePasswordScreenState) {
        when (val state = deletePasswordScreenState) {
            is ScreenState.Error -> {
                Toast.makeText(currentContext, state.message, Toast.LENGTH_SHORT).show()
            }

            is ScreenState.Loaded -> {
//                Toast.makeText(currentContext, "Password deleted!", Toast.LENGTH_SHORT).show()
                viewModel.hideDeletePasswordConfirmation()
                onClose()
            }

            else -> {}
        }
    }
    if (viewModel.deletePasswordConfirmationDialog) {
        ConfirmationDialog(
            title = "Delete ${passwordDetailsResult.passwordDetails.title}?",
            subTitle = "This will delete password permanently and is not recoverable.",
            onDismissRequest = { viewModel.hideDeletePasswordConfirmation() },
            onConfirmation = { viewModel.deletePassword(passwordId = passwordDetailsResult.passwordId) },
        )
    }
    ViewPasswordScreenContent(
        passwordDetailsResult = passwordDetailsResult,
        showPassword = viewModel.showPassword,
        onPasswordVisibilityClick = { viewModel.togglePasswordVisibility() },
        toEditPasswordScreen = { toEditPasswordScreen() },
        onClose = onClose,
        modifier = modifier,
        onDeleteClick = { viewModel.showDeletePasswordConfirmation() }
    )

}

@Composable
fun ViewPasswordScreenContent(
    passwordDetailsResult: PasswordDetailResult,
    toEditPasswordScreen: () -> Unit,
    modifier: Modifier = Modifier,
    onPasswordVisibilityClick: () -> Unit,
    onClose: () -> Unit,
    showPassword: Boolean,
    onDeleteClick: () -> Unit
) {
    val passwordDetail = passwordDetailsResult.passwordDetails
    val vault = passwordDetailsResult.vault ?: Vault.defaultVault()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = dimensionResource(R.dimen.large_padding),
                vertical = dimensionResource(R.dimen.medium_padding)
            )
            .verticalScroll(rememberScrollState())
            .imePadding(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            IconButton(
                onClick = { onClose() },
                modifier = Modifier.defaultMinSize(dimensionResource(R.dimen.min_clickable_size))
            ) {
                Icon(
                    Icons.Outlined.ArrowBackIosNew,
                    contentDescription = "Back press",
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { toEditPasswordScreen() },
                modifier = Modifier.defaultMinSize(dimensionResource(R.dimen.min_clickable_size))
            ) {
                Icon(Icons.Outlined.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Edit")
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            TitleSquare(title = if (passwordDetail.website.isNotEmpty()) passwordDetail.website else passwordDetail.title)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = passwordDetail.title,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondary,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(imageVector = vault.iconKey.toImageVector(), contentDescription = null)
                    Text(
                        text = vault.vaultName, style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Light
                        )
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .border(
                    width = 0.2.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(6.dp)
                )
        ) {
            var shownAny = false

            if (passwordDetail.email.isNotBlank()) {
                DetailItem(
                    typeName = R.string.username_or_email,
                    data = passwordDetail.email,
                    icon = Icons.Outlined.AccountBox
                )
                shownAny = true
            }

            if (passwordDetail.password.isNotBlank()) {
                if (shownAny) HorizontalDivider()
                DetailItem(
                    typeName = R.string.password,
                    data = passwordDetail.password,
                    icon = Icons.Outlined.Password,
                    onPasswordVisibilityClick = { onPasswordVisibilityClick() },
                    showPassword = showPassword,
                )
                shownAny = true
            }

            if (passwordDetail.website.isNotBlank()) {
                if (shownAny) HorizontalDivider()
                DetailItem(
                    typeName = R.string.website,
                    data = passwordDetail.website,
                    icon = Icons.Outlined.Language
                )
                shownAny = true
            }

            if (passwordDetail.notes.isNotBlank()) {
                if (shownAny) HorizontalDivider()
                DetailItem(
                    typeName = R.string.notes,
                    data = passwordDetail.notes,
                    icon = Icons.Outlined.Description
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .border(
                    width = 0.2.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(6.dp)
                )
        ) {
            var showAny = false
            if (passwordDetailsResult.modifiedAt?.isNotBlank() == true) {
                DetailItem(
                    typeName = R.string.modified,
                    data = DateTimeHelper.formatSupabaseTimestamp(passwordDetailsResult.modifiedAt), //todo: show modified
                    icon = Icons.Outlined.Edit
                )
                showAny = true
            }
            if (passwordDetailsResult.createdAt.isNotBlank()) {
                if (showAny) HorizontalDivider()
                DetailItem(
                    typeName = R.string.created,
                    data = DateTimeHelper.formatSupabaseTimestamp(passwordDetailsResult.createdAt),
                    icon = Icons.Outlined.Bolt
                )
            }
        }

        Button(
            onClick = onDeleteClick,
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.defaultMinSize(dimensionResource(R.dimen.min_clickable_size))
        ) {
            Icon(Icons.Outlined.Delete, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Delete")
        }

    }

}

@Composable
@VerticalScreenPreview
fun ViewPasswordDetailVertical() {
    ViewPasswordScreenContent(
        passwordDetailsResult = PasswordDetailResult.mockObject,
        toEditPasswordScreen = {},
        onPasswordVisibilityClick = {},
        showPassword = false,
        onClose = {},
        onDeleteClick = {},
    )
}

@Composable
fun DetailItem(
    @StringRes typeName: Int,
    data: String,
    icon: ImageVector,
    onPasswordVisibilityClick: () -> Unit = {},
    showPassword: Boolean? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Text(text = buildAnnotatedString {
            withStyle(
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light)
                    .toSpanStyle()
            ) {
                append(text = stringResource(typeName))
            }
            append(text = "\n")
            showPassword.let {
                if (it == true || it == null) append(text = data) else append(text = "•".repeat(data.length))
            }
        }, modifier = Modifier.weight(1f))
        if (typeName == R.string.password) {
            IconButton(onClick = {
                onPasswordVisibilityClick()
            }) {
                showPassword?.let {
                    Icon(
                        imageVector = if (it) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = "Password Visibility"
                    )
                }
            }
        }
    }
}

@Composable
@VerticalScreenPreview
fun DetailItemVertical() {
    DetailItem(
        typeName = R.string.username_or_email,
        data = " ",
        icon = Icons.Outlined.AccountBox
    )
}