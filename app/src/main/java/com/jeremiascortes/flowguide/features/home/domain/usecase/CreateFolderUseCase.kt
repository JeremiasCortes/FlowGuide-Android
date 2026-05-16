package com.jeremiascortes.flowguide.features.home.domain.usecase

import com.jeremiascortes.flowguide.features.home.domain.model.HomeResult
import com.jeremiascortes.flowguide.features.home.domain.repository.HomeRepository
import jakarta.inject.Inject

class CreateFolderUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(nameFolder: String, spaceId: String): HomeResult<Unit> {
        return repository.createFolder(
            nameFolder = nameFolder,
            spaceId = spaceId
        )
    }
}