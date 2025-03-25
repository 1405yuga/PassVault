package com.example.passvault.ui.screens.main_screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun Home(modifier: Modifier = Modifier) {
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    val items = listOf("List", "Add", "Profile")
    val iconsList = listOf(
        Icons.AutoMirrored.Outlined.List,
        Icons.Outlined.Add,
        Icons.Filled.Person
    )

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            //todo: add animations on switch
            when (selectedItem) {
                0 -> VaultList()
                1 -> Add()
                2 -> Profile()
            }
        }
        NavigationBar(modifier = Modifier.fillMaxWidth()) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(iconsList[index], contentDescription = item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index }
                )
            }
        }
    }
}

@Composable
@Preview
fun HomePreview() {
    Surface {
        Home()
    }
}