package com.jeremiascortes.flowguide.features.home.domain.usecase

import com.jeremiascortes.flowguide.features.home.data.model.SpaceDto
import com.jeremiascortes.flowguide.features.home.domain.repository.HomeRepository
import com.jeremiascortes.flowguide.features.home.domain.model.HomeResult
import jakarta.inject.Inject

class GetAllSpacesUseCase @Inject constructor(
    private val repository: HomeRepository
)  {
    suspend operator fun invoke(): HomeResult<List<SpaceDto>> {
        return repository.getAllSpaces()
    }
}