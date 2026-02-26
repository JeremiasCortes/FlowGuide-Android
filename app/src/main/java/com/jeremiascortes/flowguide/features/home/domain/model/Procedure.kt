package com.jeremiascortes.flowguide.features.home.domain.model

data class Procedure(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val userId: String,
    val title: String,
    val description: String,
    val steps: List<Step>
)