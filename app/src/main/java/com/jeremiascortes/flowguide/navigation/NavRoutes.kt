package com.jeremiascortes.flowguide.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoute {

    // Grupo de Autenticación
    @Serializable
    sealed class Auth : AppRoute() {

        @Serializable
        data object Splash : Auth() // Pantalla de animación de carga

        @Serializable
        data object Login : Auth()

        @Serializable
        data object Register : Auth()

        @Serializable
        data object RecoverPassword : Auth()
    }

    // Grupo de la App Principal (Home)
    @Serializable
    sealed class Main : AppRoute() {

        @Serializable
        data object Home : Main()

        @Serializable
        data object Profile : Main()

        @Serializable
        data object Notifications : Main()
    }

    // Grupo de Configuración
    @Serializable
    sealed class Settings : AppRoute() {

        @Serializable
        data object Root : Settings() // La pantalla principal de settings
        // Si tienes una pantalla de edición de perfil profunda, podría ir aquí:
        // @Serializable
        // data object EditProfile : Settings()
    }
}