package com.jeremiascortes.flowguide.features.procedure.data.repository

import com.jeremiascortes.flowguide.features.auth.di.SupabaseClient
import com.jeremiascortes.flowguide.features.procedure.data.model.ProcedureDto
import com.jeremiascortes.flowguide.features.procedure.data.model.StepDto
import com.jeremiascortes.flowguide.features.procedure.domain.model.Procedure
import com.jeremiascortes.flowguide.features.procedure.domain.model.Step
import com.jeremiascortes.flowguide.features.procedure.domain.repository.ProcedureRepository
import io.github.jan.supabase.postgrest.from
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProcedureRepositoryImpl @Inject constructor(
    val supabase: SupabaseClient
): ProcedureRepository {
    companion object {
        // Constantes relacionadas con la tabla de "procedures"
        private const val TABLE_PROCEDURE = "procedures"
        private const val TABLE_STEP = "steps"
        private const val COLUMN_PROCEDURE_ID = "id_procedure"
    }

    override suspend fun getProcedureWithSteps(id: String): Result<Procedure> {
        return try {
            val procedureDto = supabase.supabaseClient.from(TABLE_PROCEDURE)
                .select {
                    filter {
                        eq(COLUMN_PROCEDURE_ID, id)
                    }
                }
                .decodeSingle<ProcedureDto>()

            val stepsDto = supabase.supabaseClient.from(TABLE_STEP)
                .select {
                    filter {
                        eq(COLUMN_PROCEDURE_ID, id)
                    }
                }
                .decodeList<StepDto>()
            val steps = stepsDto.map { it.toDomain() }
            val procedure = procedureDto.toDomain(steps)


            Result.success(procedure)
        } catch (e: Exception) {
            Result.failure(Exception("Error al obtener el procedimiento: ${e.message ?: "Error desconocido"}"))
        }
    }

    override suspend fun updateStepCompletion(stepId: String, isCompleted: Boolean) {
        supabase.supabaseClient
            .from(TABLE_STEP)
            .update(
                mapOf(
                    "is_completed" to isCompleted
                )
            ) {
                filter {
                    eq("id_step", stepId)
                }
            }
    }

    private fun ProcedureDto.toDomain(steps: List<Step>): Procedure {
        return Procedure(
            id = idProcedure,
            name = name,
            description = description,
            isCompleted = Procedure.calculateCompleted(steps),
            steps = steps
        )
    }

    private fun StepDto.toDomain(): Step {
        return Step(
            id = idStep,
            name = name,
            description = description,
            isCompleted = isCompleted,
            order = order,
            substeps = emptyList()
        )
    }


}