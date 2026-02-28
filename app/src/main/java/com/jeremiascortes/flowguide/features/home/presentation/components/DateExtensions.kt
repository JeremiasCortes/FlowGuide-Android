package com.jeremiascortes.flowguide.features.home.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Extension function para formatear fechas ISO 8601 a texto legible.
 * Convierte timestamps como "2026-02-25T10:06:25.385533+00:00"
 * a formatos como "hace 2 días", "ayer", "hoy", o "25 Feb 2026".
 */
@Composable
fun String.formatAsRelativeTime(): String {
    return remember(this) {
        try {
            val instant = Instant.parse(this)
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            val now = LocalDateTime.now()
            val daysDiff = ChronoUnit.DAYS.between(dateTime, now)

            when {
                daysDiff == 0L -> "Hoy"
                daysDiff == 1L -> "Ayer"
                daysDiff < 7L -> "Hace $daysDiff días"
                daysDiff < 30L -> "Hace ${daysDiff / 7} semanas"
                daysDiff < 365L -> "Hace ${daysDiff / 30} meses"
                else -> dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            }
        } catch (e: Exception) {
            this
        }
    }
}
