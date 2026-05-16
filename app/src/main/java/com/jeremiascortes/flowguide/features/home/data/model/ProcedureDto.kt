package com.jeremiascortes.flowguide.features.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProcedureDto(
    @SerialName("id_procedure")
    val idProcedure: String = "",

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String = "",

    @SerialName("deleted_at")
    val deletedAt: String? = null,

    @SerialName("user_id")
    val userId: String,

    val title: String,

    val description: String? = null,

    @SerialName("folder_id")
    val idFolder: String? = null,

    @SerialName("space_id")
    val idSpace: String
)