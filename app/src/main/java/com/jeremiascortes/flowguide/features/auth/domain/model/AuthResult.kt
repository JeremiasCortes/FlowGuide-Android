package com.jeremiascortes.flowguide.features.auth.domain.model

/**
 * ============================================================================
 * CAPA: Domain - Model
 * ============================================================================
 *
 * Representa el resultado de una operación de autenticación.
 *
 * SEALED CLASS CON GENERICS:
 * - El tipo T permite devolver datos junto con el éxito
 * - Para operaciones sin datos de retorno, usar AuthResult<Unit>
 *
 * VENTAJAS SOBRE TRY-CATCH:
 * - Explícito: fuerza a manejar los tres estados
 * - Type-safe: el compilador te ayuda
 * - Predecible: no hay excepciones sorpresa
 *
 * USO EN REPOSITORIO:
 * ```kotlin
 * return try {
 *     // operación
 *     AuthResult.Success(data)
 * } catch (e: Exception) {
 *     AuthResult.Error(e.message ?: "Error desconocido")
 * }
 * ```
 *
 * USO EN UI:
 * ```kotlin
 * when (result) {
 *     is AuthResult.Loading -> CircularProgressIndicator()
 *     is AuthResult.Success -> // navegar a Home
 *     is AuthResult.Error -> // mostrar mensaje de error
 * }
 * ```
 * ============================================================================
 */

sealed class AuthResult<out T> {
    /**
     * Operación en progreso.
     * Mostrar indicador de carga.
     */
    data object Loading : AuthResult<Nothing>()

    /**
     * Operación completada exitosamente.
     * @param data Datos devueltos por la operación
     */
    data class Success<T>(val data: T) : AuthResult<T>()

    /**
     * Operación fallida.
     * @param message Mensaje de error para mostrar al usuario
     */
    data class Error(val message: String) : AuthResult<Nothing>()
}
