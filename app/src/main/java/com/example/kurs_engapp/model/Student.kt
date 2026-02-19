package com.example.kurs_engapp.model

data class Student(
    val id: Long = 0,
    val firstName: String = "",
    val lastName: String = "",
    val currentLevel: String = "Нулевой",
    val goal: String = "",
    val phoneNumber: String = "",
    val lessonDateTime: String = "",
    val avatarUri: String? = null
) {
    val fullName: String
        get() = listOf(firstName, lastName).filter { it.isNotBlank() }.joinToString(" ")
}