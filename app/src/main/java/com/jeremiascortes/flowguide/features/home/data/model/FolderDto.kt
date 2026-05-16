package com.jeremiascortes.flowguide.features.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FolderDto(

    @SerialName("id_folder")
    val idFolder: String = "",

    @SerialName("created_at")
    val createdAt: String = "",

    @SerialName("updated_at")
    val updatedAt: String = "",

    @SerialName("user_id")
    val userId: String,

    @SerialName("space_id")
    val spaceId: String,

    val title: String
)