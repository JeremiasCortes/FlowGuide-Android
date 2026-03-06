package com.jeremiascortes.flowguide.features.procedure.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProcedureDto(
    @SerialName("id_procedure")
    val idProcedure: String,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("user_id")
    val userId: String,

    @SerialName("title")
    val name: String,

    val description: String? = null,

    @SerialName("folder_id")
    val folderId: String,

    @SerialName("space_id")
    val spaceId: String
)