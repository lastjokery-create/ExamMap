package jp.maple.exammap.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.maple.exammap.model.StudyRecord
import jp.maple.exammap.ui.theme.ColorDark
import jp.maple.exammap.ui.theme.ColorGrayBg
import jp.maple.exammap.ui.theme.ColorWhite
import jp.maple.exammap.ui.theme.OrangeMain
import jp.maple.exammap.ui.viewmodel.StudyRecordViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningScreen(
    viewModel: StudyRecordViewModel,
    onRecordClick: (String) -> Unit
) {
    val records by viewModel.records.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("学習記録一覧", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = ColorDark) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorWhite)
            )
        },
        containerColor = ColorGrayBg
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            items(records) { record ->
                RecordItemCard(
                    record = record,
                    onClick = { onRecordClick(record.id) }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun RecordItemCard(
    record: StudyRecord,
    onClick: () -> Unit
) {
    val alphaValue = if (record.includedInAggregate) 1.0f else 0.55f
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alphaValue)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ColorWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = record.date.format(formatter),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )

                if (!record.includedInAggregate) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF5F5F5)
                    ) {
                        Text(
                            text = "除外",
                            color = Color(0xFF888888),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${record.score} / ${record.maxScore}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorDark
                )
                Text(
                    text = "${record.percentage}%",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangeMain
                )
            }

            if (record.note.isNotBlank()) {
                Text(
                    text = record.note,
                    fontSize = 13.sp,
                    color = ColorDark
                )
            }
        }
    }
}