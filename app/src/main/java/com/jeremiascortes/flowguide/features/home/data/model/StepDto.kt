package com.jeremiascortes.flowguide.features.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StepDto(
    @SerialName("id_step")
    val idStep: String,

    @SerialName("createdAt")
    val createdAt: String,

    @SerialName("updatedAt")
    val updatedAt: String,

    @SerialName("userId")
    val userId: String,

    @SerialName("procedureId")
    val procedureId: String,

    @SerialName("title")
    val title: String
)