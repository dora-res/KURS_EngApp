package com.example.kurs_engapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kurs_engapp.data.ProfileRepository
import com.example.kurs_engapp.model.TutorProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
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

    val profile: StateFlow<TutorProfile> =
        repository.observeProfile()
            .map { it ?: defaultProfile }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = defaultProfile
            )

    fun saveProfile(profile: TutorProfile) {
        viewModelScope.launch {
            repository.saveProfile(profile)
        }
    }
}