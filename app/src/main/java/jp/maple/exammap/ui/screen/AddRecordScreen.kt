package jp.maple.exammap.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.maple.exammap.model.StudyRecord
import jp.maple.exammap.ui.theme.ColorDark
import jp.maple.exammap.ui.theme.ColorGrayBg
import jp.maple.exammap.ui.theme.ColorWhite
import jp.maple.exammap.ui.theme.OrangeMain
import jp.maple.exammap.ui.viewmodel.StudyRecordViewModel
import java.time.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordScreen(
    examId: String,
    viewModel: StudyRecordViewModel,
    onBackClick: () -> Unit
) {
    var scoreText by remember { mutableStateOf("") }
    var maxScoreText by remember { mutableStateOf("100") }
    var noteText by remember { mutableStateOf("") }
    var excludeFromAggregate by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("学習記録の追加", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = ColorDark) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = ColorDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorWhite)
            )
        },
        containerColor = ColorGrayBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = scoreText,
                    onValueChange = { scoreText = it },
                    label = { Text("得点") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = maxScoreText,
                    onValueChange = { maxScoreText = it },
                    label = { Text("満点") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("メモ") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("集計から除外する", fontSize = 16.sp, color = ColorDark)
                    Text("ONにすると全体の進捗率・評価計算から外れます", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                }
                Switch(
                    checked = excludeFromAggregate,
                    onCheckedChange = { excludeFromAggregate = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = OrangeMain,
                        checkedTrackColor = OrangeMain.copy(alpha = 0.5f)
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val score = scoreText.toIntOrNull() ?: 0
                    val maxScore = maxScoreText.toIntOrNull() ?: 100
                    val newRecord = StudyRecord(
                        id = UUID.randomUUID().toString(),
                        examId = examId,
                        date = LocalDateTime.now(),
                        score = score,
                        maxScore = maxScore,
                        note = noteText,
                        includedInAggregate = !excludeFromAggregate
                    )
                    viewModel.addRecord(newRecord)
                    onBackClick()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = OrangeMain)
            ) {
                Text("保存する", color = ColorWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}