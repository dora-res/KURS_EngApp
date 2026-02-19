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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.kurs_engapp.data.AppDatabase
import com.example.kurs_engapp.data.ProfileRepository
import com.example.kurs_engapp.data.StudentsRepository
import com.example.kurs_engapp.screens.EditStudentScreen
import com.example.kurs_engapp.screens.EditTutorProfileScreen
import com.example.kurs_engapp.screens.ProfileScreen
import com.example.kurs_engapp.screens.StudentDetailsScreen
import com.example.kurs_engapp.screens.StudentsScreen
import com.example.kurs_engapp.viewmodel.ProfileViewModel
import com.example.kurs_engapp.viewmodel.ProfileViewModelFactory
import com.example.kurs_engapp.viewmodel.StudentsViewModel
import com.example.kurs_engapp.viewmodel.StudentsViewModelFactory

private object Routes {
    const val Profile = "profile"
    const val EditProfile = "profile/edit"

    const val Students = "students"
    const val StudentDetails = "students/{studentId}"
    fun studentDetails(studentId: Long) = "students/$studentId"

    // optional arg: studentId
    const val EditStudent = "students/edit?studentId={studentId}"
    fun editStudent(studentId: Long?) =
        if (studentId == null) "students/edit" else "students/edit?studentId=$studentId"
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

                val navController = rememberNavController()

                MaterialTheme(typography = appTypography) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.Profile
                    ) {
                        composable(Routes.Profile) {
                            ProfileScreen(
                                profile = profile,
                                onEditClick = { navController.navigate(Routes.EditProfile) },
                                onStudentsClick = { navController.navigate(Routes.Students) }
                            )
                        }

                        composable(Routes.EditProfile) {
                            EditTutorProfileScreen(
                                profile = profile,
                                onCancel = { navController.popBackStack() },
                                onSave = { updated ->
                                    profileViewModel.saveProfile(updated)
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(Routes.Students) {
                            StudentsScreen(
                                students = students,
                                onProfileClick = { navController.popBackStack() }, // назад на profile
                                onAddStudentClick = { navController.navigate(Routes.editStudent(null)) },
                                onStudentClick = { selected ->
                                    navController.navigate(Routes.studentDetails(selected.id))
                                }
                            )
                        }

                        composable(
                            route = Routes.StudentDetails,
                            arguments = listOf(
                                navArgument("studentId") { type = NavType.LongType }
                            )
                        ) { backStackEntry ->
                            val studentId = backStackEntry.arguments?.getLong("studentId") ?: 0L
                            val student = students.firstOrNull { it.id == studentId }

                            if (student == null) {
                                // если не нашли (например, удалили) — просто возвращаемся к списку
                                navController.popBackStack(Routes.Students, inclusive = false)
                            } else {
                                StudentDetailsScreen(
                                    student = student,
                                    onBackClick = { navController.popBackStack() },
                                    onEditClick = { navController.navigate(Routes.editStudent(student.id)) },
                                    onDeleteClick = { toDelete ->
                                        studentsViewModel.deleteStudent(toDelete)
                                        navController.popBackStack(Routes.Students, inclusive = false)
                                    }
                                )
                            }
                        }

                        composable(
                            route = Routes.EditStudent,
                            arguments = listOf(
                                navArgument("studentId") {
                                    type = NavType.LongType
                                    defaultValue = 0L
                                }
                            )
                        ) { backStackEntry ->
                            val studentId = backStackEntry.arguments?.getLong("studentId") ?: 0L
                            val editingStudent = students.firstOrNull { it.id == studentId }

                            EditStudentScreen(
                                student = editingStudent, // null -> добавление
                                onCancel = { navController.popBackStack() },
                                onSave = { student ->
                                    studentsViewModel.saveStudent(student)
                                    // после сохранения возвращаемся к списку
                                    navController.popBackStack(Routes.Students, inclusive = false)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}