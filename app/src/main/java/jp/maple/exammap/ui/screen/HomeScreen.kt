package jp.maple.exammap.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.maple.exammap.model.ExamStatus
import jp.maple.exammap.ui.component.ExamCard
import jp.maple.exammap.ui.theme.*
import jp.maple.exammap.ui.viewmodel.ExamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ExamViewModel,
    onAllExamsClick: () -> Unit,
    onExamClick: (String) -> Unit,
    onAddExamClick: () -> Unit
) {
    val context = LocalContext.current
    val exams by viewModel.exams.collectAsState()
    val activeExams = exams.filter { it.status == ExamStatus.PLANNED }
    val notificationCount = viewModel.getNotificationCount()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ExamMap", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = ColorDark) },
                navigationIcon = {
                    IconButton(onClick = { Toast.makeText(context, "Menu Clicked", Toast.LENGTH_SHORT).show() }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = ColorDark)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        BadgedBox(
                            badge = {
                                if (notificationCount > 0) {
                                    Badge { Text(notificationCount.toString()) }
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications", tint = ColorDark)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = ColorWhite)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddExamClick,
                containerColor = OrangeMain
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exam", tint = ColorWhite)
            }
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
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "直近の試験（${activeExams.size}件）",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = ColorDark
                    )
                }
            }

            itemsIndexed(activeExams) { index, exam ->
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

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAllExamsClick() },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ColorWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.List, contentDescription = null, tint = OrangeMain)
                        Text(
                            text = "すべて見る",
                            color = ColorDark,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)
                        )
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = ColorGrayText)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}