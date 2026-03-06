package com.jeremiascortes.flowguide.features.procedure.domain.model

data class Step(
    val id: String,
    val name: String,
    val description: String?,
    val isCompleted: Boolean,
    val order: Int? = null,
    val substeps: List<Step> = emptyList()
)