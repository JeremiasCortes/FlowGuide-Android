package com.jeremiascortes.flowguide.features.home.domain.usecase

import com.jeremiascortes.flowguide.features.home.data.model.ProcedureDto
import com.jeremiascortes.flowguide.features.home.domain.repository.HomeRepository
import com.jeremiascortes.flowguide.features.home.model.HomeResult
import jakarta.inject.Inject

class GetAllProceduresByIdFolderUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(idFolder: String): HomeResult<List<ProcedureDto>> {
        return repository.getAllProceduresByIdFolder(idFolder)
    }
}