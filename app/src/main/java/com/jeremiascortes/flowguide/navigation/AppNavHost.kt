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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jeremiascortes.flowguide.features.auth.domain.model.AuthState
import com.jeremiascortes.flowguide.features.auth.presentation.AuthViewModel
import com.jeremiascortes.flowguide.features.auth.presentation.LoginScreen
import com.jeremiascortes.flowguide.features.auth.presentation.RegisterScreen
import com.jeremiascortes.flowguide.features.home.presentation.screens.HomeScreen
import com.jeremiascortes.flowguide.features.home.presentation.viewmodel.HomeViewModel
import com.jeremiascortes.flowguide.features.procedure.presentation.screen.ProcedureScreen
import com.jeremiascortes.flowguide.features.procedure.presentation.viewmodel.ProcedureViewModel
import com.jeremiascortes.flowguide.features.settings.presentation.screen.SettingScreen
import com.jeremiascortes.flowguide.features.settings.presentation.viewmodel.SettingsViewModel
import com.jeremiascortes.flowguide.features.welcome.presentation.SplashScreen
import com.jeremiascortes.flowguide.presentation.components.BottomBar
import com.jeremiascortes.flowguide.presentation.components.BottomBarItem

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // AuthViewModel compartido: scoped a la Activity para que todas
    // las pantallas de auth compartan la misma instancia y estado
    val authViewModel: AuthViewModel = hiltViewModel()

    val bottomBarItems = remember {
        listOf(
            BottomBarItem(
                icon = Icons.Filled.Check,
                contentDescription = "Completar",
                onClick = { /* acción al hacer click */ }
            ),
            BottomBarItem(
                icon = Icons.Filled.Edit,
                contentDescription = "Editar",
                onClick = { /* acción al hacer click */ }
            ),
            BottomBarItem(
                icon = Icons.Filled.Settings,
                contentDescription = "Configuración",
                onClick = { navController.navigate(AppRoute.Settings.General) }
            )
        )
    }

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
            // Observamos el estado del ViewModel compartido
            val authState by authViewModel.authState.collectAsState()

            SplashScreen(
                onNavigate = {
                    if (authState is AuthState.Authenticated) {
                        navController.navigate(AppRoute.Main.Home) {
                            // Limpiar back stack para no volver al Splash
                            popUpTo(AppRoute.Auth.Splash) { inclusive = true }
                        }
                    } else {
                        navController.navigate(AppRoute.Auth.Login) {
                            // Limpiar back stack para no volver al Splash
                            popUpTo(AppRoute.Auth.Splash) { inclusive = true }
                        }
                    }

                }
            )
        }

        composable<AppRoute.Auth.Login> {
            // Usamos la misma instancia del ViewModel compartido
            LoginScreen(
                viewModel = authViewModel,
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
            // Usamos la misma instancia del ViewModel compartido
            RegisterScreen(
                viewModel = authViewModel,
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
            val homeViewModel: HomeViewModel = hiltViewModel()

            HomeScreen(
                homeViewModel = homeViewModel,
                onNavigateToProcedure = { idProcedure ->
                    navController.navigate(AppRoute.Main.Procedure(idProcedure)) {}
                },
                bottomBar = { BottomBar(items = bottomBarItems) }
            )
        }

        composable<AppRoute.Main.Procedure> {
            val viewModel: ProcedureViewModel = hiltViewModel()
            ProcedureScreen(
                procedureViewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable<AppRoute.Settings.General> {
            val settingsViewModel: SettingsViewModel = hiltViewModel()

            SettingScreen(
                settingsViewModel = settingsViewModel,
                authViewModel = authViewModel,
                onLogout = {
                    // Limpiar back stack para no volver a Home
                    navController.navigate(AppRoute.Auth.Splash) {
                        popUpTo(AppRoute.Settings.General) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}