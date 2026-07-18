package jp.maple.exammap.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.maple.exammap.ui.component.ExamCard
import jp.maple.exammap.ui.theme.*
import jp.maple.exammap.ui.viewmodel.ExamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamListScreen(
    viewModel: ExamViewModel,
    onExamClick: (String) -> Unit
) {
    val exams by viewModel.exams.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("試験一覧", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = ColorDark) },
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
            itemsIndexed(exams) { index, exam ->
                val colorIndex = index % 5
                val accentColor = AccentColors[colorIndex]
                val accentBadgeBg = AccentBadgeBg[colorIndex]
                val progress = viewModel.progressFor(exam.id)

                ExamCard(
                    exam = exam,
                    accentColor = accentColor,
                    accentBadgeBg = accentBadgeBg,
                    progress = progress,
                    onCardClick = { onExamClick(exam.id) }
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}