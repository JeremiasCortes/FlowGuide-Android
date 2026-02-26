package com.jeremiascortes.flowguide.features.home.domain.usecase

import com.jeremiascortes.flowguide.features.home.domain.repository.HomeRepository
import com.jeremiascortes.flowguide.features.home.model.HomeResult
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): HomeResult<Unit> {
        return repository.logout()
    }
}