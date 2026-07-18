package jp.maple.exammap.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.maple.exammap.model.Exam
import jp.maple.exammap.model.ExamDatePrecision
import jp.maple.exammap.model.ExamStatus
import jp.maple.exammap.ui.component.ExamCard
import jp.maple.exammap.ui.theme.ExamMapTheme
import jp.maple.exammap.util.ExamDateUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

private val homeResultDateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    exams: List<Exam>,
    modifier: Modifier = Modifier,
    onAddExamClick: () -> Unit,
    onExamClick: (Exam) -> Unit,
    onResultSelected: (Exam, ExamStatus) -> Unit = { _, _ -> }
) {
    // 「後で」を選んだ試験は、このアプリ起動中だけ再表示しない。
    // 次回起動時には再び確認する。
    val deferredExamIds = remember { mutableStateListOf<String>() }

    val upcomingExams = exams
        .asSequence()
        .filter { it.status == ExamStatus.PLANNED }
        .sortedWith(
            compareBy<Exam> {
                ExamDateUtils.representativeDateOrNull(it.examDate, it.datePrecision) == null
            }.thenBy {
                ExamDateUtils.representativeDateOrNull(it.examDate, it.datePrecision)
            }.thenBy { it.progress }
                .thenBy { it.shortName.ifBlank { it.name } }
        )
        .take(5)
        .toList()

    val resultInputTarget = exams
        .asSequence()
        .filter { it.status == ExamStatus.PLANNED }
        .filter { it.id !in deferredExamIds }
        .mapNotNull { exam ->
            parseResultDateOrNull(exam.resultDate)?.let { date -> exam to date }
        }
        .filter { (_, resultDate) -> !resultDate.isAfter(LocalDate.now()) }
        .sortedBy { (_, resultDate) -> resultDate }
        .map { (exam, _) -> exam }
        .firstOrNull()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = { Text("ExamMap") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddExamClick) { Text("＋") }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "直近の試験",
                style = MaterialTheme.typography.titleLarge
            )

            if (upcomingExams.isEmpty()) {
                Text(
                    text = "受験予定の試験はありません",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                upcomingExams.forEach { exam ->
                    ExamCard(
                        examName = exam.name,
                        shortName = exam.shortName,
                        date = exam.examDate,
                        datePrecision = exam.datePrecision,
                        status = exam.status,
                        progress = exam.progress,
                        onClick = { onExamClick(exam) }
                    )
                }
            }
        }
    }

    if (resultInputTarget != null) {
        AlertDialog(
            onDismissRequest = {
                deferredExamIds.add(resultInputTarget.id)
            },
            title = { Text("試験結果を入力") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(resultInputTarget.shortName.ifBlank { resultInputTarget.name })
                    Text("結果発表日を迎えました。結果を選択してください。")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onResultSelected(resultInputTarget, ExamStatus.PASSED)
                    }
                ) {
                    Text("合格")
                }
            },
            dismissButton = {
                Column {
                    TextButton(
                        onClick = {
                            onResultSelected(resultInputTarget, ExamStatus.FAILED)
                        }
                    ) {
                        Text("不合格")
                    }
                    TextButton(
                        onClick = {
                            deferredExamIds.add(resultInputTarget.id)
                        }
                    ) {
                        Text("後で")
                    }
                }
            }
        )
    }
}

private fun parseResultDateOrNull(value: String): LocalDate? {
    if (value.isBlank()) return null
    return try {
        LocalDate.parse(value, homeResultDateFormatter)
    } catch (_: DateTimeParseException) {
        null
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    ExamMapTheme {
        HomeScreen(
            exams = listOf(
                Exam(
                    id = "1",
                    name = "技術士第一次試験",
                    shortName = "技術士一次",
                    examDate = "2026/11/29",
                    datePrecision = ExamDatePrecision.EXACT,
                    status = ExamStatus.PLANNED,
                    progress = 74
                )
            ),
            onAddExamClick = {},
            onExamClick = {}
        )
    }
}
