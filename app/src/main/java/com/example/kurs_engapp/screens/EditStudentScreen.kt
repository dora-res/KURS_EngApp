package com.example.kurs_engapp.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kurs_engapp.R
import com.example.kurs_engapp.model.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.item

private val studentLevels = listOf("Нулевой", "A1", "A2", "B1", "B2", "C1", "C2", "Носитель")
private val nameRegex = Regex("[A-Za-zА-Яа-яЁё]*")
private val phoneRegex = Regex("[+0-9()\\- ]*")
private const val lessonsSeparator = "|"
private const val lessonStorageFormat = "dd.MM.yyyy HH:mm"
private const val lessonDisplayFormat = "dd.MM, HH:mm"

@Composable
fun EditStudentScreen(
    student: Student?,
    onCancel: () -> Unit,
    onSave: (Student) -> Unit
) {
    val context = LocalContext.current
    var firstName by remember(student) { mutableStateOf(student?.firstName.orEmpty()) }
    var lastName by remember(student) { mutableStateOf(student?.lastName.orEmpty()) }
    var level by remember(student) { mutableStateOf(student?.currentLevel ?: studentLevels.first()) }
    var goal by remember(student) { mutableStateOf(student?.goal.orEmpty()) }
    var phone by remember(student) { mutableStateOf(student?.phoneNumber.orEmpty()) }
    var lessonDateTimes by remember(student) {
        mutableStateOf(parseLessonValues(student?.lessonDateTime.orEmpty()))
    }
    var avatarUri by remember(student) { mutableStateOf(student?.avatarUri) }

    val pickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        val savedPhotoUri = uri?.let { copyPhotoToAppStorage(context, it) }
        avatarUri = savedPhotoUri?.toString()
    }

    val avatarBitmap by produceState<Bitmap?>(initialValue = null, key1 = avatarUri) {
        value = withContext(Dispatchers.IO) {
            avatarUri?.let { loadAvatarBitmap(context, Uri.parse(it)) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF7B53E1), Color.Transparent)
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .border(6.dp, Color(0xFF7B53E1), CircleShape)
                    .clip(CircleShape)
                    .clickable { pickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (avatarBitmap != null) {
                    Image(
                        bitmap = avatarBitmap!!.asImageBitmap(),
                        contentDescription = "Аватар ученика",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            //Spacer(modifier = Modifier.size(16.dp))

            Icon(
                painter = painterResource(R.drawable.download_basic),
                contentDescription = "Загрузить фото",
                tint = Color(0xFF4811BF),
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.BottomEnd)
                    .clickable { pickerLauncher.launch("image/*") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp)
        ) {
            item {
                StudentField(
                    value = firstName,
                    hint = "Имя",
                    onValueChange = { if (nameRegex.matches(it)) firstName = it }
                )
                StudentField(
                    value = lastName,
                    hint = "Фамилия",
                    onValueChange = { if (nameRegex.matches(it)) lastName = it }
                )
                StudentLevelDropdown(value = level, onValueChange = { level = it })
                StudentField(value = goal, hint = "Цель", onValueChange = { goal = it })
                StudentField(
                    value = phone,
                    hint = "Номер телефона",
                    keyboardType = KeyboardType.Phone,
                    onValueChange = { if (phoneRegex.matches(it)) phone = it }
                )
                StudentField(
                    value = lessonDateTimes.joinToString(" | ") { formatLessonForDisplay(it) },
                    hint = "Занятия",
                    readOnly = true,
                    onValueChange = {},
                    onClick = {
                        val calendar = Calendar.getInstance()
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                TimePickerDialog(
                                    context,
                                    { _, hourOfDay, minute ->
                                        val selectedLesson = "%02d.%02d.%04d %02d:%02d".format(
                                            dayOfMonth,
                                            month + 1,
                                            year,
                                            hourOfDay,
                                            minute
                                        )
                                        lessonDateTimes = lessonDateTimes + selectedLesson
                                    },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    true
                                ).show()
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                )
                if (lessonDateTimes.isNotEmpty()) {
                    LessonDateTimesList(
                        lessonDateTimes = lessonDateTimes,
                        onRemoveLesson = { lessonToRemove ->
                            lessonDateTimes = lessonDateTimes.filterNot { it == lessonToRemove }
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .background(Color(0xFF4811BF)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.cancel_basic),
                contentDescription = "Отмена",
                tint = Color(0xFFA58AEF),
                modifier = Modifier
                    .size(56.dp)
                    .clickable(onClick = onCancel)
            )
            Icon(
                painter = painterResource(id = R.drawable.yes_basic),
                contentDescription = "Сохранить",
                tint = Color(0xFFA58AEF),
                modifier = Modifier
                    .size(56.dp)
                    .clickable {
                        onSave(
                            Student(
                                id = student?.id ?: 0,
                                firstName = firstName,
                                lastName = lastName,
                                currentLevel = level,
                                goal = goal,
                                phoneNumber = phone,
                                lessonDateTime = lessonDateTimes.joinToString(lessonsSeparator),
                                avatarUri = avatarUri
                            )
                        )
                    }
            )
        }
    }
}

@Composable
private fun LessonDateTimesList(
    lessonDateTimes: List<String>,
    onRemoveLesson: (String) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        lessonDateTimes.forEach { lesson ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
                    .background(Color(0xFFD6C6FF), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatLessonForDisplay(lesson),
                    color = Color(0xFF4811BF)
                )
                Icon(
                    painter = painterResource(id = R.drawable.trash_basic),
                    contentDescription = "Удалить дату занятия",
                    tint = Color(0xFF4811BF),
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { onRemoveLesson(lesson) }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudentLevelDropdown(value: String, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = textFieldColors(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            placeholder = { Text("Нынешний уровень") }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            studentLevels.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onValueChange(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun StudentField(
    value: String,
    hint: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    onClick: (() -> Unit)? = null
) {
    val fieldModifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 10.dp)

    if (onClick != null) {
        Box(modifier = fieldModifier.clickable(onClick = onClick)) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false,
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                colors = textFieldColors(),
                placeholder = { Text(hint) }
            )
        }
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = fieldModifier,
            readOnly = readOnly,
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = textFieldColors(),
            placeholder = { Text(hint) }
        )
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color(0xFFA58AEF),
    unfocusedContainerColor = Color(0xFFA58AEF),
    focusedBorderColor = Color.Transparent,
    unfocusedBorderColor = Color.Transparent,
    focusedTextColor = Color(0xFF6A35DF),
    unfocusedTextColor = Color(0xFF6A35DF),
    disabledTextColor = Color(0xFF6A35DF),
    focusedPlaceholderColor = Color(0xFF7D53E1),
    unfocusedPlaceholderColor = Color(0xFF7D53E1),
    disabledPlaceholderColor = Color(0xFF7D53E1),
    disabledContainerColor = Color(0xFFA58AEF),
    disabledBorderColor = Color.Transparent
)

private fun parseLessonValues(rawValue: String): List<String> {
    if (rawValue.isBlank()) return emptyList()
    return rawValue
        .split("\n", ";", lessonsSeparator)
        .map { it.trim() }
        .filter { it.isNotBlank() }
}

private fun formatLessonForDisplay(rawValue: String): String {
    val parser = SimpleDateFormat(lessonStorageFormat, Locale("ru"))
    val formatter = SimpleDateFormat(lessonDisplayFormat, Locale("ru"))
    return try {
        val parsedDate = parser.parse(rawValue)
        if (parsedDate != null) formatter.format(parsedDate) else rawValue
    } catch (_: ParseException) {
        rawValue
    }
}