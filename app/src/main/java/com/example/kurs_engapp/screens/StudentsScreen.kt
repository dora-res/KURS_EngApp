package com.example.kurs_engapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StudentUi(
    val fullName: String,
    val level: String,
    val goal: String,
    val schedule: List<String>
)

@Composable
fun StudentsScreen(
    students: List<StudentUi>,
    onProfileClick: () -> Unit,
    onAddStudentClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF4811BF), Color.Transparent),
                        startY = -220f,
                        endY = 320f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 118.dp)
            ) {
                Text(
                    text = "Ученики",
                    color = Color(0xFF4811BF),
                    fontSize = 62.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "карточки ваших\nстудентов",
                    color = Color(0xFF9F82F0),
                    fontSize = 25.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (students.isEmpty()) {
                Text(
                    text = "Добавь учеников, нажав на плюс внизу",
                    color = Color(0xFF9F82F0),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 26.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    items(students) { student ->
                        StudentCard(student = student)
                    }
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .shadow(elevation = 14.dp)
                .background(Color(0xFF4811BF)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Профиль репетитора",
                tint = Color(0xFFA58AEF),
                modifier = Modifier
                    .size(56.dp)
                    .clickable(onClick = onProfileClick)
            )

            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Добавить ученика",
                tint = Color(0xFFA58AEF),
                modifier = Modifier
                    .size(64.dp)
                    .clickable(onClick = onAddStudentClick)
            )
        }
    }
}

@Composable
private fun StudentCard(student: StudentUi) {
    Card(
        modifier = Modifier
            .padding(horizontal = 28.dp)
            .fillMaxWidth()
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(listOf(Color(0xFF5D2BD6), Color(0xFF9D81F0))),
                shape = RoundedCornerShape(32.dp)
            ),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(94.dp)
                        .border(4.dp, Color(0xFF9F82F0), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "фото\nчелика", textAlign = TextAlign.Center, lineHeight = 18.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = student.fullName, textAlign = TextAlign.Center, lineHeight = 18.sp)
            }

            Spacer(modifier = Modifier.size(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Уровень: ${student.level}", fontSize = 30.sp)
                Text(text = "Цель: ${student.goal}", fontSize = 30.sp)
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                        .height(2.dp)
                        .background(Color(0xFF5D2BD6))
                )
                Spacer(modifier = Modifier.height(6.dp))
                student.schedule.forEach {
                    Text(text = it, fontSize = 28.sp)
                }
            }
        }
    }
}