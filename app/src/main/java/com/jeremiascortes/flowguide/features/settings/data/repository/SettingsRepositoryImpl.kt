package com.jeremiascortes.flowguide.features.settings.data.repository

import com.jeremiascortes.flowguide.features.auth.di.SupabaseClient
import com.jeremiascortes.flowguide.features.settings.domain.repository.SettingsRepository
import io.github.jan.supabase.auth.auth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    val supabase: SupabaseClient
): SettingsRepository {
    override suspend fun logout(): Result<Unit> {
        return runCatching {
            supabase.supabaseClient.auth.signOut()
        }
    }
}