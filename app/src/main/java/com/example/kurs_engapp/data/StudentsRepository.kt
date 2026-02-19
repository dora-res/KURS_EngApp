package com.example.kurs_engapp.data

import com.example.kurs_engapp.model.Student
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StudentsRepository(private val studentDao: StudentDao) {

    fun observeStudents(): Flow<List<Student>> = studentDao.observeStudents().map { list ->
        list.map { it.toModel() }
    }

    suspend fun upsertStudent(student: Student) {
        studentDao.upsert(student.toEntity())
    }
}

private fun StudentEntity.toModel(): Student = Student(
    id = id,
    firstName = firstName,
    lastName = lastName,
    currentLevel = currentLevel,
    goal = goal,
    phoneNumber = phoneNumber,
    lessonDateTime = lessonDateTime,
    avatarUri = avatarUri
)

private fun Student.toEntity(): StudentEntity = StudentEntity(
    id = id,
    firstName = firstName,
    lastName = lastName,
    currentLevel = currentLevel,
    goal = goal,
    phoneNumber = phoneNumber,
    lessonDateTime = lessonDateTime,
    avatarUri = avatarUri
)