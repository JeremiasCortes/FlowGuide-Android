package com.jeremiascortes.flowguide.features.procedure.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StepDto(
    @SerialName("id_step")
    val idStep: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("title")
    val name: String,

    val description: String?,

    val order: Int? = null,

    @SerialName("id_procedure")
    val procedureId: String,

    @SerialName("is_completed")
    val isCompleted: Boolean = false
)
