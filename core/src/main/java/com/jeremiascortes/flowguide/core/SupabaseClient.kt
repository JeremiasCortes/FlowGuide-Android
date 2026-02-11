package com.jeremiascortes.flowguide.core

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.websocket.WebSocketDeflateExtension.Companion.install
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class SupabaseClient @Inject constructor() {
    val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://supabase.flowguide.jeremiascortes.com",
        supabaseKey = "yJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYW5vbiIsImlzcyI6InN1cGFiYXNlIiwiaWF0IjoxNzY3ODI2ODAwLCJleHAiOjE5MjU1OTMyMDB9.Igo2JjTPHpdJDB55J-9lNvyGvRnlDgOybfVf-IEmFBA"
    ) {
        install(Auth)
        install(Postgrest)
    }
}
