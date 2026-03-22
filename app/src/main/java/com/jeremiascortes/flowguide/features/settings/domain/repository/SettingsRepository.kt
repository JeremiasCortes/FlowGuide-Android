package com.jeremiascortes.flowguide.features.settings.domain.repository

interface SettingsRepository {
    suspend fun logout(): Result<Unit>
}