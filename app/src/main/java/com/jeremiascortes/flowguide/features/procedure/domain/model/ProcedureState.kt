package com.jeremiascortes.flowguide.features.procedure.domain.model

data class ProcedureState(
    // Datos
    val procedure: Procedure? = null,

    // Estados de UI
    val isLoading: Boolean = false,
    val error: String? = null
)