package com.example.kurs_engapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.kurs_engapp.model.TutorProfile

class ProfileViewModel : ViewModel() {

    private val _profile = MutableStateFlow(
        TutorProfile(
            firstName = "Иван",
            lastName = "Иванов",
            middleName = "Иванович",
            subject = "Английский язык",
            experience = 5,
            level = "B2–C1"
        )
    )

    val profile = _profile.asStateFlow()
}
