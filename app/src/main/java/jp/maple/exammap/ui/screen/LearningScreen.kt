package jp.maple.exammap.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.maple.exammap.model.Exam
import jp.maple.exammap.model.RecordSortOrder
import jp.maple.exammap.model.StudyRecord

@Composable
fun LearningScreen(
    exams: List<Exam>,
    records: List<StudyRecord>,
    onAddRecordClick: (Exam) -> Unit,
    onRecordClick: (StudyRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedExamId by rememberSaveable { mutableStateOf<String?>(null) }
    var sortOrderName by rememberSaveable { mutableStateOf(RecordSortOrder.DATE_DESC.name) }
    var examMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var sortMenuExpanded by rememberSaveable { mutableStateOf(false) }

    val examMap = exams.associateBy { it.id }
    val selectedSort = RecordSortOrder.entries.firstOrNull { it.name == sortOrderName }
        ?: RecordSortOrder.DATE_DESC

    val filteredRecords = records
        .filter { selectedExamId == null || it.examId == selectedExamId }
        .let { source ->
            when (selectedSort) {
                RecordSortOrder.DATE_DESC -> source.sortedByDescending { it.studyDate }
                RecordSortOrder.DATE_ASC -> source.sortedBy { it.studyDate }
                RecordSortOrder.PERCENT_DESC -> source.sortedByDescending { it.percentage }
                RecordSortOrder.PERCENT_ASC -> source.sortedBy { it.percentage }
            }
        }

    val recordCount = filteredRecords.size
    val averagePercentage = if (recordCount == 0) 0 else filteredRecords.sumOf { it.percentage } / recordCount
    val passRate = if (recordCount == 0) 0 else filteredRecords.count { it.passed } * 100 / recordCount
    val highestPercentage = filteredRecords.maxOfOrNull { it.percentage } ?: 0
    val lowestPercentage = filteredRecords.minOfOrNull { it.percentage } ?: 0

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("学習分析", style = MaterialTheme.typography.headlineMedium)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatisticCard("実施回数", "${recordCount}回", Modifier.weight(1f))
                StatisticCard("平均正答率", "${averagePercentage}%", Modifier.weight(1f))
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatisticCard("基準達成率", "${passRate}%", Modifier.weight(1f))
                StatisticCard("最高 / 最低", "${highestPercentage}% / ${lowestPercentage}%", Modifier.weight(1f))
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("表示条件", style = MaterialTheme.typography.titleMedium)

                OutlinedButton(
                    onClick = { examMenuExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val selectedExam = selectedExamId?.let(examMap::get)
                    Text(
                        selectedExam?.shortName?.takeIf { it.isNotBlank() }
                            ?: selectedExam?.name
                            ?: "すべての試験"
                    )
                }
                DropdownMenu(
                    expanded = examMenuExpanded,
                    onDismissRequest = { examMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("すべての試験") },
                        onClick = {
                            selectedExamId = null
                            examMenuExpanded = false
                        }
                    )
                    exams.forEach { exam ->
                        DropdownMenuItem(
                            text = { Text(exam.shortName.takeIf { it.isNotBlank() } ?: exam.name) },
                            onClick = {
                                selectedExamId = exam.id
                                examMenuExpanded = false
                            }
                        )
                    }
                }

                OutlinedButton(
                    onClick = { sortMenuExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(selectedSort.label)
                }
                DropdownMenu(
                    expanded = sortMenuExpanded,
                    onDismissRequest = { sortMenuExpanded = false }
                ) {
                    RecordSortOrder.entries.forEach { order ->
                        DropdownMenuItem(
                            text = { Text(order.label) },
                            onClick = {
                                sortOrderName = order.name
                                sortMenuExpanded = false
                            }
                        )
                    }
                }
            }
        }

        item {
            Text("記録を追加", style = MaterialTheme.typography.titleLarge)
        }

        if (exams.isEmpty()) {
            item { Text("先に試験を登録してください") }
        } else {
            items(exams, key = { "add_${it.id}" }) { exam ->
                Button(
                    onClick = { onAddRecordClick(exam) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("${exam.shortName.takeIf { it.isNotBlank() } ?: exam.name}に記録")
                }
            }
        }

        item {
            Text("履歴", style = MaterialTheme.typography.titleLarge)
        }

        if (filteredRecords.isEmpty()) {
            item { Text("条件に一致する学習記録はありません") }
        } else {
            items(filteredRecords, key = { it.id }) { record ->
                val exam = examMap[record.examId]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRecordClick(record) }
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            exam?.shortName?.takeIf { it.isNotBlank() }
                                ?: exam?.name
                                ?: "削除済み試験",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(record.studyDate)
                            Text("${record.score}/${record.maxScore}（${record.percentage}%）")
                        }
                        if (record.pastExamId.isNotBlank()) Text(record.pastExamId)
                        Text(if (record.passed) "★ 基準達成" else "- 未達")
                        if (record.memo.isNotBlank()) Text(record.memo)
                        Text("タップして編集", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun StatisticCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium)
            Text(value, style = MaterialTheme.typography.titleLarge)
        }
    }
}
