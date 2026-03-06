package com.jeremiascortes.flowguide.features.procedure.domain.repository

import com.jeremiascortes.flowguide.features.procedure.domain.model.Procedure

interface ProcedureRepository {
    suspend fun getProcedureWithSteps(id: String): Result<Procedure>
    suspend fun updateStepCompletion(stepId: String, isCompleted: Boolean)
}