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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.kurs_engapp.data.AppDatabase
import com.example.kurs_engapp.data.ProfileRepository
import com.example.kurs_engapp.data.StudentsRepository
import com.example.kurs_engapp.model.Student
import com.example.kurs_engapp.screens.EditStudentScreen
import com.example.kurs_engapp.screens.EditTutorProfileScreen
import com.example.kurs_engapp.screens.ProfileScreen
import com.example.kurs_engapp.screens.StudentsScreen
import com.example.kurs_engapp.viewmodel.ProfileViewModel
import com.example.kurs_engapp.viewmodel.ProfileViewModelFactory
import com.example.kurs_engapp.viewmodel.StudentsViewModel
import com.example.kurs_engapp.viewmodel.StudentsViewModelFactory

private enum class ScreenRoute {
    Profile,
    EditProfile,
    Students,
    EditStudent
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
        val profileRepository = ProfileRepository(database.tutorProfileDao())
        val studentsRepository = StudentsRepository(database.studentDao())
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
                    factory = ProfileViewModelFactory(profileRepository)
                )
                val studentsViewModel: StudentsViewModel = viewModel(
                    factory = StudentsViewModelFactory(studentsRepository)
                )
                val profile by profileViewModel.profile.collectAsState()
                val students by studentsViewModel.students.collectAsState()
                var currentScreen by rememberSaveable { mutableStateOf(ScreenRoute.Profile) }
                var editingStudent by remember { mutableStateOf<Student?>(null) }

                MaterialTheme(typography = appTypography) {
                    when (currentScreen) {
                        ScreenRoute.Profile -> ProfileScreen(
                            profile = profile,
                            onEditClick = { currentScreen = ScreenRoute.EditProfile },
                            onStudentsClick = { currentScreen = ScreenRoute.Students }
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
                            students = students,
                            onProfileClick = { currentScreen = ScreenRoute.Profile },
                            onAddStudentClick = {
                                editingStudent = null
                                currentScreen = ScreenRoute.EditStudent
                            },
                            onStudentClick = { selected ->
                                editingStudent = selected
                                currentScreen = ScreenRoute.EditStudent
                            }
                        )

                        ScreenRoute.EditStudent -> EditStudentScreen(
                            student = editingStudent,
                            onCancel = {
                                currentScreen = ScreenRoute.Students
                            },
                            onSave = { student ->
                                studentsViewModel.saveStudent(student)
                                currentScreen = ScreenRoute.Students
                            }
                        )
                    }
                }
            }
        }
    }
}