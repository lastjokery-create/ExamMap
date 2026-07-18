package jp.maple.exammap.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.maple.exammap.ui.component.ExamDatePickerField
import jp.maple.exammap.model.ExamDatePrecision
import jp.maple.exammap.ui.component.ExamDatePrecisionField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExamScreen(
    onBackClick: () -> Unit,
    onRegisterClick: (
        String,
        String,
        String,
        ExamDatePrecision
    ) -> Unit
) {
    var examName by rememberSaveable { mutableStateOf("") }
    var shortName by rememberSaveable { mutableStateOf("") }
    var examDate by rememberSaveable { mutableStateOf("") }
    var datePrecision by rememberSaveable {
        mutableStateOf(ExamDatePrecision.EXACT)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("試験登録") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = examName,
                onValueChange = { examName = it },
                label = { Text("試験名") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = shortName,
                onValueChange = { shortName = it },
                label = { Text("略称") },
                placeholder = { Text("例：技術士一次") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            ExamDatePrecisionField(
                selectedPrecision = datePrecision,
                onPrecisionSelected = { selected ->
                    datePrecision = selected

                    examDate = when {
                        selected == ExamDatePrecision.UNDECIDED -> {
                            ""
                        }

                        selected == ExamDatePrecision.EXACT &&
                                examDate.length == 7 -> {
                            ""
                        }

                        selected != ExamDatePrecision.EXACT &&
                                examDate.length == 10 -> {
                            examDate.take(7)
                        }

                        else -> {
                            examDate
                        }
                    }
                }
            )

            ExamDatePickerField(
                examDate = examDate,
                datePrecision = datePrecision,
                onDateSelected = { selectedDate ->
                    examDate = selectedDate
                }
            )

            Button(
                onClick = {
                    onRegisterClick(
                        examName,
                        shortName,
                        examDate,
                        datePrecision
                    )
                },
                enabled = examName.isNotBlank() &&
                        (
                                datePrecision == ExamDatePrecision.UNDECIDED ||
                                        examDate.isNotBlank()
                                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("登録")
            }
        }
    }
}