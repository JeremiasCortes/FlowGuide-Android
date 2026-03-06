package com.jeremiascortes.flowguide.features.home.domain.usecase

import com.jeremiascortes.flowguide.features.home.data.model.StepDto
import com.jeremiascortes.flowguide.features.home.domain.repository.HomeRepository
import com.jeremiascortes.flowguide.features.home.domain.model.HomeResult
import jakarta.inject.Inject

class GetAllStepsByIdProcedureUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(idProcedure: String): HomeResult<List<StepDto>> {
        return repository.getAllStepsByIdProcedure(idProcedure)
    }
}