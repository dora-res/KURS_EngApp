package com.example.kurs_engapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kurs_engapp.data.StudentsRepository
import com.example.kurs_engapp.model.Student
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudentsViewModel(
    private val repository: StudentsRepository
) : ViewModel() {

    val students: StateFlow<List<Student>> = repository.observeStudents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun saveStudent(student: Student) {
        viewModelScope.launch {
            repository.upsertStudent(student)
        }
    }
}