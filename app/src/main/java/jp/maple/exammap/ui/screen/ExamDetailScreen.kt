package jp.maple.exammap.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.maple.exammap.model.Exam
import jp.maple.exammap.model.ExamDatePrecision
import jp.maple.exammap.model.ExamStatus
import jp.maple.exammap.ui.component.ExamDatePickerField
import jp.maple.exammap.ui.component.ExamDatePrecisionField
import jp.maple.exammap.util.ExamDateUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private val resultDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamDetailScreen(
    exam: Exam,
    onBackClick: () -> Unit,
    onSaveClick: (Exam) -> Unit,
    onDeleteClick: (Exam) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable(exam.id) { mutableStateOf(exam.name) }
    var shortName by rememberSaveable(exam.id) { mutableStateOf(exam.shortName) }
    var examDate by rememberSaveable(exam.id) { mutableStateOf(exam.examDate) }
    var datePrecisionName by rememberSaveable(exam.id) {
        mutableStateOf(exam.datePrecision.name)
    }
    var resultDate by rememberSaveable(exam.id) { mutableStateOf(exam.resultDate) }
    var statusName by rememberSaveable(exam.id) { mutableStateOf(exam.status.name) }
    var progress by rememberSaveable(exam.id) { mutableIntStateOf(exam.progress) }
    var statusMenuExpanded by remember { mutableStateOf(false) }
    var showArchiveDialog by remember { mutableStateOf(false) }

    val datePrecision = ExamDatePrecision.entries.firstOrNull {
        it.name == datePrecisionName
    } ?: ExamDatePrecision.UNDECIDED

    val status = ExamStatus.entries.firstOrNull {
        it.name == statusName
    } ?: ExamStatus.PLANNED

    val isReadOnly = exam.status == ExamStatus.PASSED
    val resultDateError = validateResultDate(
        examDate = examDate,
        precision = datePrecision,
        resultDate = resultDate
    )
    val canSave = name.isNotBlank() && resultDateError == null

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("試験詳細", style = MaterialTheme.typography.headlineMedium)

        if (isReadOnly) {
            Text("合格済みのため閲覧専用です", color = MaterialTheme.colorScheme.primary)
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("正式名称") },
            singleLine = true,
            readOnly = isReadOnly,
            enabled = !isReadOnly,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = shortName,
            onValueChange = { shortName = it },
            label = { Text("略称") },
            placeholder = { Text("例：AP、技術士一次") },
            singleLine = true,
            readOnly = isReadOnly,
            enabled = !isReadOnly,
            modifier = Modifier.fillMaxWidth()
        )

        ExamDatePrecisionField(
            selectedPrecision = datePrecision,
            onPrecisionSelected = { selected ->
                if (!isReadOnly) {
                    datePrecisionName = selected.name
                    examDate = when {
                        selected == ExamDatePrecision.UNDECIDED -> ""
                        selected == ExamDatePrecision.EXACT && examDate.length == 7 -> ""
                        selected != ExamDatePrecision.EXACT && examDate.length == 10 -> examDate.take(7)
                        else -> examDate
                    }
                }
            }
        )

        ExamDatePickerField(
            examDate = examDate,
            datePrecision = datePrecision,
            onDateSelected = { examDate = it },
            enabled = !isReadOnly
        )

        ExamDatePickerField(
            examDate = resultDate,
            datePrecision = ExamDatePrecision.EXACT,
            onDateSelected = { resultDate = it },
            label = "結果発表日（任意）",
            enabled = !isReadOnly
        )

        if (!isReadOnly && resultDate.isNotBlank()) {
            TextButton(
                onClick = { resultDate = "" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("結果発表日をクリア")
            }
        }

        if (resultDateError != null) {
            Text(
                text = resultDateError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        ExposedDropdownMenuBox(
            expanded = statusMenuExpanded,
            onExpandedChange = {
                if (!isReadOnly) statusMenuExpanded = !statusMenuExpanded
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = status.displayName,
                onValueChange = {},
                readOnly = true,
                enabled = !isReadOnly,
                label = { Text("受験ステータス") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusMenuExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = statusMenuExpanded,
                onDismissRequest = { statusMenuExpanded = false }
            ) {
                ExamStatus.entries
                    .filter { it != ExamStatus.ARCHIVED }
                    .forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.displayName) },
                            onClick = {
                                statusName = option.name
                                statusMenuExpanded = false
                            }
                        )
                    }
            }
        }

        Text("進捗：$progress%")

        Slider(
            value = progress.toFloat(),
            onValueChange = { newValue ->
                progress = newValue.toInt().coerceIn(0, 100)
            },
            enabled = !isReadOnly,
            valueRange = 0f..100f,
            steps = 19,
            modifier = Modifier.fillMaxWidth()
        )

        if (!isReadOnly) {
            Button(
                onClick = {
                    onSaveClick(
                        exam.copy(
                            name = name.trim(),
                            shortName = shortName.trim(),
                            examDate = examDate.trim(),
                            datePrecision = datePrecision,
                            resultDate = resultDate.trim(),
                            status = status,
                            progress = progress
                        )
                    )
                },
                enabled = canSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("保存")
            }
        }

        OutlinedButton(
            onClick = { showArchiveDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("アーカイブへ移動")
        }

        TextButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("戻る")
        }
    }

    if (showArchiveDialog) {
        AlertDialog(
            onDismissRequest = { showArchiveDialog = false },
            title = { Text("アーカイブへ移動しますか？") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(exam.shortName.ifBlank { exam.name })
                    Text("ホーム・試験一覧・学習画面から非表示になります。")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showArchiveDialog = false
                        onDeleteClick(exam)
                    }
                ) {
                    Text("移動")
                }
            },
            dismissButton = {
                TextButton(onClick = { showArchiveDialog = false }) {
                    Text("キャンセル")
                }
            }
        )
    }
}

private fun validateResultDate(
    examDate: String,
    precision: ExamDatePrecision,
    resultDate: String
): String? {
    if (resultDate.isBlank()) return null

    val parsedResultDate = try {
        LocalDate.parse(resultDate, resultDateFormatter)
    } catch (_: DateTimeParseException) {
        return "結果発表日の形式が不正です"
    }

    val representativeExamDate = ExamDateUtils.representativeDateOrNull(
        examDate = examDate,
        precision = precision
    ) ?: return null

    return if (parsedResultDate.isBefore(representativeExamDate)) {
        "結果発表日は試験日以降に設定してください"
    } else {
        null
    }
}
