package jp.maple.exammap.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import jp.maple.exammap.model.Exam
import jp.maple.exammap.model.ExamStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveScreen(
    exams: List<Exam>,
    onBackClick: () -> Unit,
    onRestoreClick: (Exam) -> Unit
) {
    val archivedExams = exams
        .filter { it.status == ExamStatus.ARCHIVED }
        .sortedWith(compareBy<Exam> { it.displayOrder }.thenBy { it.name })

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("アーカイブ") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { Text("←") }
                }
            )
        }
    ) { innerPadding ->
        if (archivedExams.isEmpty()) {
            Text(
                text = "アーカイブされた試験はありません",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(innerPadding).padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(archivedExams, key = { it.id }) { exam ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = exam.shortName.ifBlank { exam.name },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "元の状態：${(exam.statusBeforeArchive ?: ExamStatus.PLANNED).displayName}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Button(
                                onClick = { onRestoreClick(exam) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("復元")
                            }
                        }
                    }
                }
            }
        }
    }
}
