package com.example.kurs_engapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.kurs_engapp.data.AppDatabase
import com.example.kurs_engapp.data.ProfileRepository
import com.example.kurs_engapp.screens.EditTutorProfileScreen
import com.example.kurs_engapp.screens.ProfileScreen
import com.example.kurs_engapp.screens.StudentsScreen
import com.example.kurs_engapp.viewmodel.ProfileViewModel
import com.example.kurs_engapp.viewmodel.ProfileViewModelFactory

private enum class ScreenRoute {
    Profile,
    EditProfile,
    Students
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "eng_app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
        val repository = ProfileRepository(database.tutorProfileDao())
        val gropledFontFamily = FontFamily(Font(R.font.gropled_bold))
        val appTypography = Typography().run {
            Typography(
                displayLarge = displayLarge.copy(fontFamily = gropledFontFamily),
                displayMedium = displayMedium.copy(fontFamily = gropledFontFamily),
                displaySmall = displaySmall.copy(fontFamily = gropledFontFamily),
                headlineLarge = headlineLarge.copy(fontFamily = gropledFontFamily),
                headlineMedium = headlineMedium.copy(fontFamily = gropledFontFamily),
                headlineSmall = headlineSmall.copy(fontFamily = gropledFontFamily),
                titleLarge = titleLarge.copy(fontFamily = gropledFontFamily),
                titleMedium = titleMedium.copy(fontFamily = gropledFontFamily),
                titleSmall = titleSmall.copy(fontFamily = gropledFontFamily),
                bodyLarge = bodyLarge.copy(fontFamily = gropledFontFamily),
                bodyMedium = bodyMedium.copy(fontFamily = gropledFontFamily),
                bodySmall = bodySmall.copy(fontFamily = gropledFontFamily),
                labelLarge = labelLarge.copy(fontFamily = gropledFontFamily),
                labelMedium = labelMedium.copy(fontFamily = gropledFontFamily),
                labelSmall = labelSmall.copy(fontFamily = gropledFontFamily)
            )
        }

        setContent {
            Surface(color = Color.White) {
                val profileViewModel: ProfileViewModel = viewModel(
                    factory = ProfileViewModelFactory(repository)
                )
                val profile by profileViewModel.profile.collectAsState()
                var currentScreen by rememberSaveable { mutableStateOf(ScreenRoute.Profile) }

                MaterialTheme(typography = appTypography) {
                    when (currentScreen) {
                        ScreenRoute.Profile -> ProfileScreen(
                            profile = profile,
                            onEditClick = { currentScreen = ScreenRoute.EditProfile },
                            onStudentsClick = {currentScreen = ScreenRoute.Students}
                        )

                        ScreenRoute.EditProfile -> EditTutorProfileScreen(
                            profile = profile,
                            onCancel = { currentScreen = ScreenRoute.Profile },
                            onSave = { updatedProfile ->
                                profileViewModel.saveProfile(updatedProfile)
                                currentScreen = ScreenRoute.Profile
                            }
                        )
                        ScreenRoute.Students -> StudentsScreen(
                            students = emptyList(),
                            onProfileClick = { currentScreen = ScreenRoute.Profile },
                            onAddStudentClick = { }
                        )
                    }
                }
            }
        }
    }
}