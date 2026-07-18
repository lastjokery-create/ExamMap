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
import jp.maple.exammap.ui.theme.ColorDark
import jp.maple.exammap.ui.theme.ColorGrayBg
import jp.maple.exammap.ui.theme.ColorWhite
import jp.maple.exammap.ui.theme.OrangeMain
import jp.maple.exammap.ui.viewmodel.StudyRecordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecordScreen(
    recordId: String,
    viewModel: StudyRecordViewModel,
    onBackClick: () -> Unit
) {
    val record = viewModel.getRecordById(recordId)

    if (record == null) {
        LaunchedEffect(Unit) { onBackClick() }
        return
    }

    var scoreText by remember { mutableStateOf(record.score.toString()) }
    var maxScoreText by remember { mutableStateOf(record.maxScore.toString()) }
    var noteText by remember { mutableStateOf(record.note) }
    var excludeFromAggregate by remember { mutableStateOf(!record.includedInAggregate) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("学習記録の編集", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = ColorDark) },
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
                    val updated = record.copy(
                        score = score,
                        maxScore = maxScore,
                        note = noteText,
                        includedInAggregate = !excludeFromAggregate
                    )
                    viewModel.updateRecord(updated)
                    onBackClick()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = OrangeMain)
            ) {
                Text("変更を保存する", color = ColorWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}