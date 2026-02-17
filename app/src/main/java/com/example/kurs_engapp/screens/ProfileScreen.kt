package com.example.kurs_engapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kurs_engapp.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {

    val profile by viewModel.profile.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // 🌈 Верхний градиент
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF8C5CF2),
                            Color.Transparent
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Репетитор",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4811BF)
                )
                Text(
                    text = "это вы",
                    fontSize = 18.sp,
                    color = Color(0xFFAD8DF2)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 👤 Аватар
        Box(
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .background(Color(0xFFEDE7F6)),
            contentAlignment = Alignment.Center
        ) {
            Text("Фото")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 👇 ФИО
        Text(
            text = "${profile.lastName} ${profile.firstName}\n${profile.middleName}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 📋 Карточка информации
        Card(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("${profile.subject}")
                Text("Стаж ${profile.experience} лет")
                Text("Уровень ${profile.level}")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 🔽 Нижняя панель
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(Color(0xFF4811BF)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Исправленная строка - добавьте нужный drawable ID
            Icon(
                painter = painterResource(id = R.drawable.ic_students), // Замените на ваш drawable
                contentDescription = "Ученики",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}