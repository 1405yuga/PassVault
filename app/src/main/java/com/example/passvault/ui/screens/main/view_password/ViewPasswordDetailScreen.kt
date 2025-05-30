package com.example.passvault.ui.screens.main.view_password

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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.passvault.R
import com.example.passvault.data.Vault
import com.example.passvault.utils.annotations.VerticalScreenPreview
import com.example.passvault.utils.custom_composables.TitleSquare
import com.example.passvault.utils.extension_functions.HandleScreenState
import com.example.passvault.utils.extension_functions.toImageVector

@Composable
fun ViewPasswordDetailScreen(
    passwordId: Long?,
    viewModel: ViewPasswordDetailViewModel,
    vault: Vault,
    toEditPasswordScreen: (passwordDetailResult: PasswordDetailResult) -> Unit,
    modifier: Modifier = Modifier
) {
    val screenState by viewModel.screenState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadPasswordDetails(passwordId = passwordId)
    }
    HandleScreenState(state = screenState, onLoaded = { passwordDetailsResult ->
        ViewPasswordScreenContent(
            passwordDetailsResult = passwordDetailsResult,
            vault = vault,
            toEditPasswordScreen = { toEditPasswordScreen(it) },
            modifier = modifier
        )
    })
}

@Composable
fun ViewPasswordScreenContent(
    passwordDetailsResult: PasswordDetailResult,
    vault: Vault,
    toEditPasswordScreen: (passwordDetailResult: PasswordDetailResult) -> Unit,
    modifier: Modifier = Modifier
) {
    val passwordDetail = passwordDetailsResult.passwordDetails
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = dimensionResource(R.dimen.large_padding),
                vertical = dimensionResource(R.dimen.medium_padding)
            )
            .verticalScroll(rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = {
// TODO: back press
                },
                modifier = Modifier.defaultMinSize(dimensionResource(R.dimen.min_clickable_size))
            ) {
                Icon(
                    Icons.Outlined.ArrowBackIosNew,
                    contentDescription = "Back press",
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { toEditPasswordScreen(passwordDetailsResult) },
                modifier = Modifier.defaultMinSize(dimensionResource(R.dimen.min_clickable_size))
            ) {
                Icon(Icons.Outlined.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Edit")
            }
            IconButton(onClick = {
// TODO: open bottom modal screen n display options
            }, modifier = Modifier.defaultMinSize(dimensionResource(R.dimen.min_clickable_size))) {
                Icon(Icons.Default.MoreVert, contentDescription = "More")
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleSquare(title = passwordDetail.title)
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
            modifier = Modifier.border(
                width = 0.2.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(6.dp)
            )
        ) {
            DetailItem(
                typeName = R.string.username_or_email,
                data = passwordDetail.email,
                icon = Icons.Outlined.AccountBox
            )
            HorizontalDivider()
            DetailItem(
                typeName = R.string.password,
                data = passwordDetail.password,
                icon = Icons.Outlined.Password
            )
            DetailItem(
                typeName = R.string.website,
                data = passwordDetail.website,
                icon = Icons.Outlined.Language
            )
            DetailItem(
                typeName = R.string.notes,
                data = passwordDetail.notes,
                icon = Icons.Outlined.Description
            )
        }
        Column(
            modifier = Modifier.border(
                width = 0.2.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(6.dp)
            )
        ) {
            DetailItem(
                typeName = R.string.modified,
                data = "29th May 2025", //todo: show modified
                icon = Icons.Outlined.Edit
            )
            HorizontalDivider()
            DetailItem(
                typeName = R.string.created,
                data = "20th May 2025", // todo: show created
                icon = Icons.Outlined.Bolt
            )
        }

    }

}

@Composable
@VerticalScreenPreview
fun ViewPasswordDetailVertical() {
    ViewPasswordScreenContent(
        passwordDetailsResult = PasswordDetailResult.mockObject,
        vault = Vault.defaultVault(),
        toEditPasswordScreen = {}
    )
}

@Composable
fun DetailItem(@StringRes typeName: Int, data: String, icon: ImageVector) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
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
            append(text = "\n$data")
        }, modifier = Modifier.weight(1f))
        if (typeName == R.string.password) {
            IconButton(onClick = {

            }) { Icon(Icons.Outlined.VisibilityOff, contentDescription = "Password Visibility") }
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