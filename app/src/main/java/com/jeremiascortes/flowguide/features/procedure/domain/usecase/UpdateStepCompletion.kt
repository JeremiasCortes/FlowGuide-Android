package com.jeremiascortes.flowguide.features.procedure.domain.usecase

import android.util.Log
import com.jeremiascortes.flowguide.features.procedure.domain.repository.ProcedureRepository
import javax.inject.Inject

class UpdateStepCompletion @Inject constructor(
    private val procedureRepository: ProcedureRepository
) {
    suspend operator fun invoke(stepId: String, isCompleted: Boolean): Result<Unit> {
        return try {
            procedureRepository.updateStepCompletion(stepId, isCompleted)
            Log.d("JACC", "Creo que ha ido todo bien")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("JACC", "Error al actualizar step", e)
            Result.failure(Exception("Error al actualizar step"))
        }
    }
}