package com.example.kurs_engapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kurs_engapp.data.StudentsRepository
import com.example.kurs_engapp.model.Student
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentsViewModel @Inject constructor(
    private val repository: StudentsRepository
) : ViewModel() {

    val students: StateFlow<List<Student>> =
        repository.observeStudents()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveStudent(student: Student) {
        viewModelScope.launch { repository.upsertStudent(student) }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch { repository.deleteStudent(student) }
    }
}