package com.jeremiascortes.flowguide.features.auth.data.repository

/**
 * ============================================================================
 * CAPA: Data - Repository Implementation
 * ============================================================================
 *
 * Implementación concreta de AuthRepository que se conecta con Supabase.
 *
 * ARQUITECTURA CLEAN ARCHITECTURE:
 * - Esta clase pertenece a la capa de Data (la más externa)
 * - Implementa la interfaz de dominio (AuthRepository)
 * - Depende de frameworks externos (Supabase) pero el dominio no sabe nada de esto
 *
 * INYECCIÓN DE DEPENDENCIAS:
 * - @Singleton: Solo hay una instancia en toda la app
 * - @Inject: Hilt crea la instancia automáticamente
 * - SupabaseClient se inyecta como dependencia
 *
 * PATRÓN REPOSITORIO:
 * - Abstrae la fuente de datos (Supabase) del resto de la app
 * - Convierte excepciones a AuthResult para manejo limpio de errores
 * - El dominio nunca sabe si los datos vienen de Supabase, Firebase, etc.
 * ============================================================================
 */

import com.jeremiascortes.flowguide.features.auth.di.SupabaseClient
import com.jeremiascortes.flowguide.features.auth.domain.model.AuthResult
import com.jeremiascortes.flowguide.features.auth.domain.model.AuthState
import com.jeremiascortes.flowguide.features.auth.domain.repository.AuthRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : AuthRepository {

    /**
     * Inicia sesión con email y contraseña.
     * Usa signInWith(Email) de Supabase.
     */
    override suspend fun login(email: String, password: String): AuthResult<Unit> {
        return try {
            supabaseClient.supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error desconocido")
        }
    }

    /**
     * Registra un nuevo usuario con email, contraseña y datos adicionales.
     *
     * IMPORTANTE - Datos del usuario (metadata):
     * - Se envían en el campo 'data' usando buildJsonObject
     * - Estos datos se guardan en raw_user_meta_data de Supabase
     * - Se pueden acceder después desde session.user.userMetadata
     * - Supabase Auth Hooks pueden usar estos datos para crear perfil en otra tabla
     */
    override suspend fun register(
        name: String,
        email: String,
        birthday: String,
        password: String,
        confirmPassword: String
    ): AuthResult<Unit> {
        return try {
            supabaseClient.supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
                // Metadata del usuario: datos adicionales que se guardan con la cuenta
                data = buildJsonObject {
                    put("name", name)
                    put("birthday", birthday)
                }
            }
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error desconocido")
        }
    }

    /**
     * Autenticación con Google OAuth.
     *
     * FUNCIONAMIENTO:
     * - Si el usuario no existe, lo crea automáticamente
     * - Si existe, lo loguea
     * - Por eso funciona tanto para "login" como para "registro"
     *
     * PARÁMETROS IMPORTANTES:
     * - automaticallyOpenUrl = false: Manejamos la URL nosotros (deep links)
     * - queryParams["prompt"] = "select_account": Fuerza selector de cuenta
     *   (sin esto, usa la última cuenta de Google automáticamente)
     */
    override suspend fun signInWithGoogle(): AuthResult<Unit> {
        return try {
            supabaseClient.supabaseClient.auth.signInWith(Google) {
                automaticallyOpenUrl = false
                queryParams["prompt"] = "select_account"
            }
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error desconocido")
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     * Elimina la sesión tanto en el cliente como en Supabase.
     */
    override suspend fun logout(): AuthResult<Unit> {
        return try {
            supabaseClient.supabaseClient.auth.signOut()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Error desconocido")
        }
    }

    /**
     * Obtiene el estado actual de la sesión.
     * Se usa al iniciar la app para saber si el usuario ya está logueado.
     *
     * RETORNO:
     * - AuthState.Authenticated(userId): Hay sesión activa
     * - AuthState.NotAuthenticated: No hay sesión
     */
    override suspend fun getCurrentSession(): AuthState {
        return try {
            val session = supabaseClient.supabaseClient.auth.currentSessionOrNull()
            if (session == null) {
                AuthState.NotAuthenticated
            } else {
                AuthState.Authenticated(session.user?.id ?: "")
            }
        } catch (e: Exception) {
            AuthState.NotAuthenticated
        }
    }
}
