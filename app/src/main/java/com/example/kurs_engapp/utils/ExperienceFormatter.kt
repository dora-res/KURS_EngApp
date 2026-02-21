package com.example.kurs_engapp.utils

fun formatExperienceLabel(experience: String): String {
    val years = experience.toIntOrNull() ?: return "Стаж —"

    val suffix = when {
        years % 100 in 11..14 -> "лет"
        years % 10 == 1 -> "год"
        years % 10 in 2..4 -> "года"
        else -> "лет"
    }

    return "Стаж $years $suffix"
}