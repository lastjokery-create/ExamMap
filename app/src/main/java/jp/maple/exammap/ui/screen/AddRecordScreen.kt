package jp.maple.exammap.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import jp.maple.exammap.model.Exam
import jp.maple.exammap.model.ExamDatePrecision
import jp.maple.exammap.ui.component.ExamDatePickerField

@Composable
fun AddRecordScreen(
    exam: Exam,
    onBackClick: () -> Unit,
    onRegisterClick: (
        pastExamId: String,
        studyDate: String,
        score: Int,
        maxScore: Int,
        passed: Boolean,
        memo: String
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var pastExamId by rememberSaveable { mutableStateOf("") }
    var studyDate by rememberSaveable { mutableStateOf("") }
    var scoreText by rememberSaveable { mutableStateOf("") }
    var maxScoreText by rememberSaveable { mutableStateOf("") }
    var passed by rememberSaveable { mutableStateOf(false) }
    var memo by rememberSaveable { mutableStateOf("") }

    val score = scoreText.toIntOrNull()
    val maxScore = maxScoreText.toIntOrNull()
    val canSave = studyDate.isNotBlank() && score != null && maxScore != null && maxScore > 0 && score in 0..maxScore

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("学習記録を追加")
        Text(if (exam.shortName.isNotBlank()) exam.shortName else exam.name)

        OutlinedTextField(
            value = pastExamId,
            onValueChange = { pastExamId = it },
            label = { Text("過去問ID") },
            placeholder = { Text("例：2025年度") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        ExamDatePickerField(
            examDate = studyDate,
            datePrecision = ExamDatePrecision.EXACT,
            onDateSelected = { studyDate = it }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = scoreText,
                onValueChange = { scoreText = it.filter(Char::isDigit) },
                label = { Text("得点") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = maxScoreText,
                onValueChange = { maxScoreText = it.filter(Char::isDigit) },
                label = { Text("満点") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = passed, onCheckedChange = { passed = it })
            Text("合格基準を達成")
        }

        OutlinedTextField(
            value = memo,
            onValueChange = { memo = it },
            label = { Text("メモ") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onRegisterClick(pastExamId, studyDate, score ?: 0, maxScore ?: 0, passed, memo) },
            enabled = canSave,
            modifier = Modifier.fillMaxWidth()
        ) { Text("保存") }

        TextButton(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) { Text("戻る") }
    }
}
