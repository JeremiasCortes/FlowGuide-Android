package com.jeremiascortes.flowguide.navigation

/**
 * ============================================================================
 * NAVEGACIÓN - NavHost
 * ============================================================================
 *
 * Configuración central de navegación de la aplicación.
 *
 * TECNOLOGÍAS:
 * - Navigation Compose: Navegación declarativa
 * - Type-safe routes: Usamos data objects con @Serializable
 *
 * ESTRUCTURA DE RUTAS:
 * - AppRoute.Auth: Splash, Login, Register
 * - AppRoute.Main: Home
 *
 * ANIMACIONES:
 * - slideInHorizontally + fadeIn: Entrada
 * - slideOutHorizontally + fadeOut: Salida
 *
 * BACK STACK:
 * - popUpTo: Limpia el back stack al navegar
 * - inclusive = true: Incluye la pantalla actual en la limpieza
 *
 * VIEWMODEL SCOPING:
 * - hiltViewModel(): Crea/obtiene el ViewModel
 * - Mismo NavController = misma instancia de ViewModel
 * ============================================================================
 */

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jeremiascortes.flowguide.features.auth.presentation.AuthViewModel
import com.jeremiascortes.flowguide.features.auth.presentation.LoginScreen
import com.jeremiascortes.flowguide.features.auth.presentation.RegisterScreen
import com.jeremiascortes.flowguide.features.home.presentation.screen.HomeScreen
import com.jeremiascortes.flowguide.features.home.presentation.viewmodel.HomeViewModel
import com.jeremiascortes.flowguide.features.welcome.presentation.SplashScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.Auth.Splash,
        // Animaciones por defecto para todas las transiciones
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        // ==================== AUTH FLOW ====================

        composable<AppRoute.Auth.Splash> {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(AppRoute.Auth.Login) {
                        // Limpiar back stack para no volver al Splash
                        popUpTo(AppRoute.Auth.Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<AppRoute.Auth.Login> {
            val viewModel: AuthViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = {
                    navController.navigate(AppRoute.Auth.Register)
                },
                onNavigateToHome = {
                    navController.navigate(AppRoute.Main.Home) {
                        // Limpiar back stack para no volver a Login
                        popUpTo(AppRoute.Auth.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<AppRoute.Auth.Register> {
            val viewModel: AuthViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.navigate(AppRoute.Auth.Login)
                },
                onNavigateToHome = {
                    navController.navigate(AppRoute.Main.Home) {
                        // Limpiar back stack para no volver a Register
                        popUpTo(AppRoute.Auth.Register) { inclusive = true }
                    }
                }
            )
        }

        // ==================== MAIN FLOW ====================

        composable<AppRoute.Main.Home> {
            val viewModel: HomeViewModel = hiltViewModel()

            HomeScreen(
                homeViewModel = viewModel,
                onNavigateToSplash = {
                    navController.navigate(AppRoute.Auth.Splash) {
                        popUpTo(AppRoute.Main.Home) { inclusive = true }
                    }
                }
            )
        }
    }
}
