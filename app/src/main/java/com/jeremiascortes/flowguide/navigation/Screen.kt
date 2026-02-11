package com.jeremiascortes.flowguide.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("Splash")
    object Register : Screen("Register")
    object Login : Screen("Login")
    object Onboarding : Screen("Onboarding")
    object Home : Screen("Home")
    object Profile : Screen("Profile")
    object Settings : Screen("Settings")
}