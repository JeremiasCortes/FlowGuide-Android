package com.jeremiascortes.flowguide.features.auth.di

/**
 * ============================================================================
 * CAPA: DI (Dependency Injection) - External Service Wrapper
 * ============================================================================
 *
 * Wrapper del cliente de Supabase que se inyecta en los repositorios.
 *
 * POR QUÉ UN WRAPPER Y NO USAR SUPABASE DIRECTAMENTE:
 * 1. Abstracción: Si mañana cambias a Firebase, solo cambias este archivo
 * 2. Testabilidad: Puedes crear un FakeSupabaseClient para tests
 * 3. Configuración centralizada: Toda la config de Supabase en un lugar
 *
 * CONFIGURACIÓN OAUTH:
 * - scheme: El esquema del deep link (flowguide://)
 * - host: El host del deep link (flowguide://login)
 * - Esto permite que la app reciba el callback después del login con Google
 *
 * SEGURIDAD:
 * - La supabaseKey aquí mostrada es la clave ANON (pública)
 * - NUNCA pongas la service_role key en el cliente
 * - En producción, usa BuildConfig o secretos del gradle
 * ============================================================================
 */

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class SupabaseClient @Inject constructor() {

    val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://supabase.flowguide.jeremiascortes.com",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYW5vbiIsImlzcyI6InN1cGFiYXNlIiwiaWF0IjoxNzY3ODI2ODAwLCJleHAiOjE5MjU1OTMyMDB9.Igo2JjTPHpdJDB55J-9lNvyGvRnlDgOybfVf-IEmFBA"
    ) {
        install(Auth) {
            // Configuración del deep link para OAuth (Google, etc.)
            // flowguide://login es la URL que recibirá el callback
            scheme = "flowguide"
            host = "login"
        }
        install(Postgrest)
    }
}
