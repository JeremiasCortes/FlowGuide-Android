package com.jeremiascortes.flowguide.features.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ============================================================================
 * DTO: Data Transfer Object
 * ============================================================================
 *
 * Objeto que representa datos recibidos desde el backend (Supabase).
 * Se usa en la capa de DATA para transformar la respuesta JSON en objetos Kotlin.
 *
 * DIFERENCIA CON MODELO DE DOMINIO:
 * - DTOs vienen del backend y tienen estructura que puede cambiar
 * - Modelos de dominio son estables y contienen solo datos necesarios
 * - Si cambias la estructura del backend, actualizas DTOs, no modelos
 * ============================================================================
 */

@Serializable
data class SpaceDto(
    @SerialName("id_space")
    val idSpace: String,

    @SerialName("user_id")
    val userId: String,

    val title: String,
    @SerialName("created_at")

    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String
)

