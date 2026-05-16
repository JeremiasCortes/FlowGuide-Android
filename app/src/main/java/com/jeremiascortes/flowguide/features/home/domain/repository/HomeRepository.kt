package com.jeremiascortes.flowguide.features.home.domain.repository

import com.jeremiascortes.flowguide.features.home.data.model.FolderDto
import com.jeremiascortes.flowguide.features.home.data.model.ProcedureDto
import com.jeremiascortes.flowguide.features.home.data.model.SpaceDto
import com.jeremiascortes.flowguide.features.home.data.model.StepDto
import com.jeremiascortes.flowguide.features.home.domain.model.HomeResult

interface HomeRepository {
    suspend fun logout(): HomeResult<Unit>
    suspend fun getAllSpaces(): HomeResult<List<SpaceDto>>
    suspend fun getAllFoldersByIdSpace(idSpace: String): HomeResult<List<FolderDto>>
    suspend fun getAllProceduresByIdSpace(idSpace: String): HomeResult<List<ProcedureDto>>
    suspend fun getAllProceduresByIdFolder(idFolder: String): HomeResult<List<ProcedureDto>>
    suspend fun getAllStepsByIdProcedure(idProcedure: String): HomeResult<List<StepDto>>
    suspend fun createSpace(nameSpace: String): HomeResult<Unit>
    suspend fun updateSpace(spaceDto: SpaceDto): HomeResult<Unit>
    suspend fun deleteSpace(spaceDto: SpaceDto): HomeResult<Unit>
    suspend fun createFolder(nameFolder: String, spaceId: String): HomeResult<Unit>
}