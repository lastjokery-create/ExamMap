package jp.maple.exammap.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import jp.maple.exammap.model.ExamDatePrecision
import jp.maple.exammap.util.ExamDateUtils
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamDatePickerField(
    examDate: String,
    datePrecision: ExamDatePrecision,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val fieldText = ExamDateUtils.displayDateText(
        examDate = examDate,
        precision = datePrecision
    )

    val fieldEnabled = enabled && datePrecision != ExamDatePrecision.UNDECIDED
    val resolvedLabel = label ?: if (datePrecision == ExamDatePrecision.EXACT) {
        "試験日"
    } else {
        "対象月"
    }

    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = fieldText,
            onValueChange = {},
            readOnly = true,
            enabled = fieldEnabled,
            label = { Text(resolvedLabel) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier
                .matchParentSize()
                .clickable(enabled = fieldEnabled) {
                    showDialog = true
                }
        )
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            val selectedDate = Instant
                                .ofEpochMilli(selectedMillis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()

                            val pattern = if (datePrecision == ExamDatePrecision.EXACT) {
                                "yyyy/MM/dd"
                            } else {
                                "yyyy/MM"
                            }

                            onDateSelected(
                                selectedDate.format(DateTimeFormatter.ofPattern(pattern))
                            )
                        }
                        showDialog = false
                    }
                ) {
                    Text("決定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("キャンセル")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
