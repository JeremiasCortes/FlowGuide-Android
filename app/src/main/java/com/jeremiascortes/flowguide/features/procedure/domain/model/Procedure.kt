package com.jeremiascortes.flowguide.features.procedure.domain.model

data class Procedure(
    val id: String,
    val name: String,
    val description: String?,
    val isCompleted: Boolean,
    val steps: List<Step>
) {
    companion object {
        fun calculateCompleted(steps: List<Step>): Boolean {
            return steps.all { step ->
                step.isCompleted && (step.substeps.isEmpty() || calculateCompleted(step.substeps))
            }
        }
    }
}
