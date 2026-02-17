package com.jeremiascortes.flowguide.features.auth.domain.model

/**
 * ============================================================================
 * CAPA: Domain - Model
 * ============================================================================
 *
 * Representa el estado de autenticación del usuario en la aplicación.
 *
 * SEALED CLASS:
 * - Permite representar estados mutuamente excluyentes
 * - El compilador verifica que se manejen todos los casos en when()
 * - Ideal para estados de UI que vienen del dominio
 *
 * USO EN VIEWMODEL:
 * ```kotlin
 * private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
 * val authState: StateFlow<AuthState> = _authState.asStateFlow()
 * ```
 *
 * USO EN UI:
 * ```kotlin
 * when (authState) {
 *     is AuthState.Loading -> CircularProgressIndicator()
 *     is AuthState.Authenticated -> HomeScreen()
 *     is AuthState.NotAuthenticated -> LoginScreen()
 * }
 * ```
 * ============================================================================
 */

sealed class AuthState {
    /**
     * Estado inicial mientras se verifica si hay sesión activa.
     * Mostrar loading o splash screen.
     */
    data object Loading : AuthState()

    /**
     * Usuario autenticado correctamente.
     * @param userId ID del usuario en Supabase
     */
    data class Authenticated(val userId: String) : AuthState()

    /**
     * Usuario no autenticado.
     * Mostrar pantalla de login.
     */
    data object NotAuthenticated : AuthState()
}
