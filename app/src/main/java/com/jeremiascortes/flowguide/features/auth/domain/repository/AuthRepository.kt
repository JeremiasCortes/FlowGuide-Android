package com.jeremiascortes.flowguide.features.auth.domain.repository

/**
 * ============================================================================
 * CAPA: Domain - Repository Interface
 * ============================================================================
 *
 * Interfaz que define las operaciones de autenticación disponibles.
 *
 * ARQUITECTURA CLEAN ARCHITECTURE:
 * - Esta interfaz pertenece a la capa de Domain (el núcleo)
 * - NO depende de frameworks externos (Supabase, Firebase, etc.)
 * - Define QUÉ se puede hacer, no CÓMO se hace
 *
 * PRINCIPIO DE INVERSIÓN DE DEPENDENCIAS:
 * - La capa de presentation (ViewModel) depende de esta interfaz
 * - La capa de data (AuthRepositoryImpl) implementa esta interfaz
 * - Así el dominio controla la abstracción
 *
 * PARA NUEVAS FEATURES:
 * Copia este patrón:
 * 1. Define la interfaz en domain/repository/
 * 2. Implementa en data/repository/
 * 3. Inyecta en Hilt (di/Module.kt)
 * ============================================================================
 */

import com.jeremiascortes.flowguide.features.auth.domain.model.AuthResult
import com.jeremiascortes.flowguide.features.auth.domain.model.AuthState

interface AuthRepository {

    /**
     * Inicia sesión con email y contraseña.
     * @return AuthResult.Success si funciona, AuthResult.Error si falla
     */
    suspend fun login(email: String, password: String): AuthResult<Unit>

    /**
     * Autenticación con Google OAuth.
     * Funciona tanto para login como para registro (si no existe, lo crea).
     */
    suspend fun signInWithGoogle(): AuthResult<Unit>

    /**
     * Registra un nuevo usuario.
     * @param name Nombre del usuario
     * @param email Email del usuario
     * @param birthday Fecha de nacimiento (YYYY-MM-DD)
     * @param password Contraseña
     * @param confirmPassword Confirmación de contraseña
     */
    suspend fun register(
        name: String,
        email: String,
        birthday: String,
        password: String,
        confirmPassword: String
    ): AuthResult<Unit>

    /**
     * Cierra la sesión del usuario actual.
     */
    suspend fun logout(): AuthResult<Unit>

    /**
     * Obtiene el estado actual de la sesión.
     * Útil para saber si el usuario ya está logueado al iniciar la app.
     */
    suspend fun getCurrentSession(): AuthState
}
