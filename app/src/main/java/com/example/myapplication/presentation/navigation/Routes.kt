package com.example.myapplication.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    data object HomeScreen:Routes()
    data object SearchScreen:Routes()
    data object FavoritesScreen:Routes()
    data class FullImageScreen(val imageId: String) : Routes()
    data class ProfileScreen(val profileLink: String):Routes()
}