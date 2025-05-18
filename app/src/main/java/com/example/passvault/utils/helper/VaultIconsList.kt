package com.example.passvault.utils.helper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

object VaultIconsList {
    fun getIconsList(): List<ImageVector> = listOf(
        Icons.Default.Home,
        Icons.Default.Person,
        Icons.Default.Language,
        Icons.Default.Work,
        Icons.Default.Lock,
        Icons.Default.AttachMoney,
        Icons.Default.Favorite,
        Icons.Default.PermMedia,
    )
}