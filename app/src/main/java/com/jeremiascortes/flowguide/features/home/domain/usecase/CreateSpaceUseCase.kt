package com.jeremiascortes.flowguide.features.home.domain.usecase

import com.jeremiascortes.flowguide.features.home.domain.model.HomeResult
import com.jeremiascortes.flowguide.features.home.domain.repository.HomeRepository
import jakarta.inject.Inject

class CreateSpaceUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(nameSpace: String): HomeResult<Unit> {
        return repository.createSpace(nameSpace)
    }
}