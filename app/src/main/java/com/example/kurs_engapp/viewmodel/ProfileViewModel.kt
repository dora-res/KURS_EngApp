package com.example.kurs_engapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kurs_engapp.data.ProfileRepository
import com.example.kurs_engapp.model.TutorProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val defaultProfile = TutorProfile(
        firstName = "Имя",
        lastName = "Фамилия",
        middleName = "Отчество",
        subject = "Специализация",
        experience = "—",
        level = "—",
        avatarUri = null
    )

    val profile: StateFlow<TutorProfile> = repository.observeProfile()
        .map { saved -> saved ?: defaultProfile }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), defaultProfile)

    fun saveProfile(updatedProfile: TutorProfile) {
        viewModelScope.launch {
            repository.saveProfile(updatedProfile)
        }
    }
}

