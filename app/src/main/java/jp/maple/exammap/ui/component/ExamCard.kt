package jp.maple.exammap.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import jp.maple.exammap.model.ExamDatePrecision
import jp.maple.exammap.model.ExamStatus
import jp.maple.exammap.util.ExamDateUtils

@Composable
fun ExamCard(
    examName: String,
    shortName: String,
    date: String,
    datePrecision: ExamDatePrecision,
    status: ExamStatus,
    progress: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayName = shortName.ifBlank { examName }
    val normalizedProgress = progress.coerceIn(0, 100)
    val statusColor = when (status) {
        ExamStatus.PLANNED -> MaterialTheme.colorScheme.primary
        ExamStatus.PASSED -> Color(0xFF2E7D32)
        ExamStatus.FAILED -> MaterialTheme.colorScheme.error
        ExamStatus.ARCHIVED -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = ExamDateUtils.displayDateText(date, datePrecision),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = ExamDateUtils.remainingDaysText(date, datePrecision),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = status.displayName,
                color = statusColor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            LinearProgressIndicator(
                progress = { normalizedProgress / 100f },
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "進捗 $normalizedProgress%",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
