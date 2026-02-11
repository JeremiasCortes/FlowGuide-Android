package com.jeremiascortes.flowguide.navigation


import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jeremiascortes.flowguide.ui.features.home.Home
import com.jeremiascortes.flowguide.ui.welcome.Splash

@Composable
fun Navigation() {
    // Crear el navController usando rememberNavController()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.Auth.Splash,
        // Definimos animaciones por defecto para toda la app
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it }, // Entra desde la derecha
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it }, // Sale por la izquierda
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it }, // Entra desde la izquierda al volver atrás
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it }, // Sale por la derecha al volver atrás
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        composable<AppRoute.Auth.Splash> {
            Splash(
                onNavigateToHome = {
                    // Navegar a Login después de 2.5 segundos
                    navController.navigate(AppRoute.Main.Home) {
                        // Limpiar el back stack para que el usuario no pueda volver al Splash
                        popUpTo(AppRoute.Auth.Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<AppRoute.Main.Home> {
            Home(
                onNavigateToSplash = {
                    // Navegar de vuelta al Splash (solo para demostración, normalmente no harías esto)
                    navController.navigate(AppRoute.Auth.Splash) {
                        // Limpiar el back stack para evitar múltiples instancias
                        popUpTo(AppRoute.Main.Home) { inclusive = true }
                    }
                }
            )
        }
    }
}