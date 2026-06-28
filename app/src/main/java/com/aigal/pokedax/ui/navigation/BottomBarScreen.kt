package com.aigal.pokedax.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    object Saved : BottomBarScreen(
        route = "saved",
        title = "Saved",
        icon = Icons.Default.Bookmark
    )

    object Profile : BottomBarScreen(
        route = "profile",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}
