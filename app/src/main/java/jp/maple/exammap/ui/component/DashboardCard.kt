package jp.maple.exammap.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.maple.exammap.model.Exam
import jp.maple.exammap.model.ExamStatus

@Composable
fun DashboardCard(
    exams: List<Exam>,
    modifier: Modifier = Modifier
) {
    val totalCount = exams.size

    val studyingCount = exams.count {
        it.status == ExamStatus.PLANNED
    }

    val passedCount = exams.count {
        it.status == ExamStatus.PASSED
    }

    val averageProgress = if (exams.isEmpty()) {
        0
    } else {
        exams.sumOf {
            it.progress.coerceIn(0, 100)
        } / exams.size
    }

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "ダッシュボード",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            DashboardRow(
                label = "登録試験数",
                value = "${totalCount}件"
            )

            DashboardRow(
                label = "勉強中",
                value = "${studyingCount}件"
            )

            DashboardRow(
                label = "合格",
                value = "${passedCount}件"
            )

            DashboardRow(
                label = "平均進捗",
                value = "${averageProgress}%"
            )
        }
    }
}

@Composable
private fun DashboardRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}