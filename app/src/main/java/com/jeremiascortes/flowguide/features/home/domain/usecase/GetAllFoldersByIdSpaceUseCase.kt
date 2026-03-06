package com.jeremiascortes.flowguide.features.home.domain.usecase

import com.jeremiascortes.flowguide.features.home.data.model.FolderDto
import com.jeremiascortes.flowguide.features.home.domain.repository.HomeRepository
import com.jeremiascortes.flowguide.features.home.domain.model.HomeResult
import jakarta.inject.Inject

class GetAllFoldersByIdSpaceUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(idSpace: String): HomeResult<List<FolderDto>> {
        return repository.getAllFoldersByIdSpace(idSpace)
    }
}