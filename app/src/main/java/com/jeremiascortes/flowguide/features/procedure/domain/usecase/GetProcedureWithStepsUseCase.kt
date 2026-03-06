package com.jeremiascortes.flowguide.features.procedure.domain.usecase

import com.jeremiascortes.flowguide.features.procedure.domain.model.Procedure
import com.jeremiascortes.flowguide.features.procedure.domain.repository.ProcedureRepository
import javax.inject.Inject


class GetProcedureWithStepsUseCase @Inject constructor(
    private val repository: ProcedureRepository
){
    suspend operator fun invoke(idProcedure: String): Result<Procedure> {
        return repository.getProcedureWithSteps(idProcedure)
    }
}