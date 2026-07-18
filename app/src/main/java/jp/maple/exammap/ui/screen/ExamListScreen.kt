package jp.maple.exammap.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.maple.exammap.model.Exam
import jp.maple.exammap.model.ExamStatus
import jp.maple.exammap.ui.component.ExamCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamListScreen(
    exams: List<Exam>,
    onAddExamClick: () -> Unit,
    onExamClick: (Exam) -> Unit,
    onAcquiredQualificationsClick: () -> Unit,
    onArchiveClick: () -> Unit
) {
    val visibleExams = exams
        .filter { it.status == ExamStatus.PLANNED || it.status == ExamStatus.FAILED }
        .sortedWith(compareBy<Exam> { it.displayOrder }.thenBy { it.name })

    Scaffold(
        topBar = { TopAppBar(title = { Text("試験") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddExamClick) { Text("＋") }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onAcquiredQualificationsClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("取得済み資格")
                    }
                    OutlinedButton(
                        onClick = onArchiveClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("アーカイブ")
                    }
                }
            }

            if (visibleExams.isEmpty()) {
                item {
                    Text(
                        text = "登録中の試験はありません",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                items(visibleExams, key = { it.id }) { exam ->
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
