package com.example.passvault.ui.screens.main.view_password

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.passvault.R
import com.example.passvault.di.supabase.SupabaseModule
import com.example.passvault.network.supabase.EncryptedDataRepository
import com.example.passvault.utils.annotations.VerticalScreenPreview

@Composable
fun ViewPasswordDetailScreen(
    passwordId: Long?,
    viewModel: ViewPasswordDetailViewModel,
    modifier: Modifier = Modifier
) {
    Text(text = "Password Id : $passwordId")
    LaunchedEffect(Unit) {
        viewModel.loadPasswordDetails(passwordId = passwordId)
    }
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(
//                horizontal = dimensionResource(R.dimen.large_padding),
//                vertical = dimensionResource(R.dimen.medium_padding)
//            )
//            .verticalScroll(rememberScrollState())
//            .imePadding(),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
//            IconButton(
//                onClick = {
//// TODO: back press
//                },
//                modifier = Modifier.defaultMinSize(dimensionResource(R.dimen.min_clickable_size))
//            ) {
//                Icon(
//                    Icons.Outlined.ArrowBackIosNew,
//                    contentDescription = "Back press",
//                )
//            }
//            Spacer(modifier = Modifier.weight(1f))
//            Button(onClick = {
//                // TODO: go to edit screen
//            }, modifier = Modifier.defaultMinSize(dimensionResource(R.dimen.min_clickable_size))) {
//                Icon(Icons.Outlined.Edit, contentDescription = null)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(text = "Edit")
//            }
//            IconButton(onClick = {
//// TODO: open bottom modal screen n display options
//            }, modifier = Modifier.defaultMinSize(dimensionResource(R.dimen.min_clickable_size))) {
//                Icon(Icons.Default.MoreVert, contentDescription = "More")
//            }
//        }
//
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            TitleSquare(title = passwordDetails.title)
//            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
//                Text(
//                    text = passwordDetails.title,
//                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
//                )
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(2.dp),
//                    modifier = Modifier
//                        .border(
//                            width = 1.dp,
//                            color = MaterialTheme.colorScheme.secondary,
//                            shape = RoundedCornerShape(30.dp)
//                        )
//                        .padding(horizontal = 8.dp, vertical = 2.dp)
//                ) {
//                    Icon(imageVector = vault.iconKey.toImageVector(), contentDescription = null)
//                    Text(
//                        text = vault.vaultName, style = MaterialTheme.typography.bodyMedium.copy(
//                            fontWeight = FontWeight.Light
//                        )
//                    )
//                }
//            }
//        }
//
//        Column(
//            modifier = Modifier.border(
//                width = 0.2.dp,
//                color = MaterialTheme.colorScheme.secondary,
//                shape = RoundedCornerShape(6.dp)
//            )
//        ) {
//            DetailItem(
//                typeName = R.string.username_or_email,
//                data = passwordDetails.email,
//                icon = Icons.Outlined.AccountBox
//            )
//            HorizontalDivider()
//            DetailItem(
//                typeName = R.string.password,
//                data = passwordDetails.password,
//                icon = Icons.Outlined.Password
//            )
//            DetailItem(
//                typeName = R.string.website,
//                data = passwordDetails.website,
//                icon = Icons.Outlined.Language
//            )
//            DetailItem(
//                typeName = R.string.notes,
//                data = passwordDetails.notes,
//                icon = Icons.Outlined.Description
//            )
//        }
//        Column(
//            modifier = Modifier.border(
//                width = 0.2.dp,
//                color = MaterialTheme.colorScheme.secondary,
//                shape = RoundedCornerShape(6.dp)
//            )
//        ) {
//            DetailItem(
//                typeName = R.string.modified,
//                data = "29th May 2025", //todo: show modified
//                icon = Icons.Outlined.Edit
//            )
//            HorizontalDivider()
//            DetailItem(
//                typeName = R.string.created,
//                data = "20th May 2025", // todo: show created
//                icon = Icons.Outlined.Bolt
//            )
//        }
//
//    }
}

@Composable
@VerticalScreenPreview
fun ViewPasswordDetailVertical() {
    ViewPasswordDetailScreen(
        passwordId = 0L,
        viewModel = ViewPasswordDetailViewModel(
            encryptedDataRepository = EncryptedDataRepository(
                supabaseClient = SupabaseModule.mockClient
            )
        )
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