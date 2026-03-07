package com.jeremiascortes.flowguide.features.procedure.domain.usecase

import com.jeremiascortes.flowguide.features.procedure.domain.repository.ProcedureRepository
import javax.inject.Inject

class UpdateStepCompletion @Inject constructor(
    private val procedureRepository: ProcedureRepository
) {
    suspend operator fun invoke(stepId: String, isCompleted: Boolean): Result<Unit> {
        return try {
            procedureRepository.updateStepCompletion(stepId, isCompleted)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al actualizar step"))
        }
    }
}