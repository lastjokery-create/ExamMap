package jp.maple.exammap.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import jp.maple.exammap.model.ExamStatus

@Composable
fun StatusFilterBar(
    selected: ExamStatus?,
    onSelected: (ExamStatus?) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FilterChip(
            selected = selected == null,
            onClick = { onSelected(null) },
            label = { Text("すべて") },
            colors = FilterChipDefaults.filterChipColors()
        )

        ExamStatus.entries.forEach { status ->
            FilterChip(
                selected = selected == status,
                onClick = { onSelected(status) },
                label = {
                    Text(status.displayName)
                },
                colors = FilterChipDefaults.filterChipColors()
            )
        }
    }
}