package com.jeremiascortes.flowguide.features.home.domain.usecase

import com.jeremiascortes.flowguide.features.home.data.model.ProcedureDto
import com.jeremiascortes.flowguide.features.home.domain.repository.HomeRepository
import com.jeremiascortes.flowguide.features.home.model.HomeResult
import jakarta.inject.Inject

class GetAllProceduresByIdSpaceUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(idSpace: String): HomeResult<List<ProcedureDto>> {
        return repository.getAllProceduresByIdSpace(idSpace)
    }
}