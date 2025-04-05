package com.example.passvault.ui.screens.main_screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.passvault.R

@Composable
fun Add(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    //todo : implement close
                },
                modifier = Modifier
                    .size(dimensionResource(R.dimen.min_clickable_size))
            ) {
                Icon(
                    Icons.Outlined.Clear,
                    contentDescription = "close window",
                )
            }
            Button(
                onClick = {
                    //todo: on create
                },
                modifier = Modifier.height(dimensionResource(R.dimen.min_clickable_size))
            ) { Text(text = "Create") }
        }
    }
}

@Composable
@Preview
fun AddPreview() {
    Add()
}