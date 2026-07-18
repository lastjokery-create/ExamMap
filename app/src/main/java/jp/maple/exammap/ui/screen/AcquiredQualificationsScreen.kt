package jp.maple.exammap.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.maple.exammap.model.Exam
import jp.maple.exammap.model.ExamStatus
import jp.maple.exammap.ui.component.ExamCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcquiredQualificationsScreen(
    exams: List<Exam>,
    onBackClick: () -> Unit,
    onExamClick: (Exam) -> Unit
) {
    val passedExams = exams
        .filter { it.status == ExamStatus.PASSED }
        .sortedWith(compareBy<Exam> { it.displayOrder }.thenBy { it.name })

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("取得済み資格") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Text("←") }
                }
            )
        }
    ) { innerPadding ->
        if (passedExams.isEmpty()) {
            Text(
                text = "取得済み資格はありません",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(innerPadding).padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(passedExams, key = { it.id }) { exam ->
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
}
