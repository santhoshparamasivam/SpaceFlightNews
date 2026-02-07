package com.example.spaceflightnewsapp.presentation.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtils {

    private val outputFormatter =
        DateTimeFormatter.ofPattern("dd MMM yyyy")

    fun formatIsoDate(isoString: String): String {
        return try {
            Instant.parse(isoString)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(outputFormatter)
        } catch (e: Exception) {
            ""
        }
    }
}
