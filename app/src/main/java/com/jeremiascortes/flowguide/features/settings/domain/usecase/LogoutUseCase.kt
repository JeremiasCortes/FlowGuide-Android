package com.jeremiascortes.flowguide.features.settings.domain.usecase

import com.jeremiascortes.flowguide.features.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.logout()
    }
}