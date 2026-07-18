package jp.maple.exammap.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import jp.maple.exammap.model.Exam
import jp.maple.exammap.model.ExamDatePrecision
import jp.maple.exammap.model.StudyRecord
import jp.maple.exammap.ui.component.ExamDatePickerField

@Composable
fun EditRecordScreen(
    exam: Exam,
    record: StudyRecord,
    onBackClick: () -> Unit,
    onSaveClick: (String, String, Int, Int, Boolean, String) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pastExamId by rememberSaveable { mutableStateOf(record.pastExamId) }
    var studyDate by rememberSaveable { mutableStateOf(record.studyDate) }
    var scoreText by rememberSaveable { mutableStateOf(record.score.toString()) }
    var maxScoreText by rememberSaveable { mutableStateOf(record.maxScore.toString()) }
    var passed by rememberSaveable { mutableStateOf(record.passed) }
    var memo by rememberSaveable { mutableStateOf(record.memo) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    val score = scoreText.toIntOrNull()
    val maxScore = maxScoreText.toIntOrNull()
    val canSave = studyDate.isNotBlank() && score != null && maxScore != null &&
        maxScore > 0 && score in 0..maxScore

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("学習記録を編集")
        Text(if (exam.shortName.isNotBlank()) exam.shortName else exam.name)

        OutlinedTextField(
            value = pastExamId,
            onValueChange = { pastExamId = it },
            label = { Text("過去問ID") },
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
            onClick = {
                onSaveClick(
                    pastExamId,
                    studyDate,
                    score ?: 0,
                    maxScore ?: 0,
                    passed,
                    memo
                )
            },
            enabled = canSave,
            modifier = Modifier.fillMaxWidth()
        ) { Text("更新") }

        TextButton(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) { Text("この記録を削除") }

        TextButton(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
            Text("戻る")
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("学習記録を削除") },
            text = { Text("この記録を削除します。元に戻せません。") },
            confirmButton = {
                TextButton(onClick = onDeleteClick) { Text("削除") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("キャンセル") }
            }
        )
    }
}
