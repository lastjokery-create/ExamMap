package jp.maple.exammap.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.maple.exammap.ui.theme.ColorDark
import jp.maple.exammap.ui.theme.ColorGrayBg
import jp.maple.exammap.ui.theme.ColorWhite
import jp.maple.exammap.ui.theme.OrangeMain
import jp.maple.exammap.ui.viewmodel.ExamViewModel
import jp.maple.exammap.util.EvaluationBand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamDetailScreen(
    examId: String,
    viewModel: ExamViewModel,
    onBackClick: () -> Unit
) {
    val exams by viewModel.exams.collectAsState()
    val exam = exams.find { it.id == examId }

    if (exam == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("試験が見つかりません", color = ColorDark)
        }
        return
    }

    val evalA = viewModel.evaluationAFor(examId)
    val evalB = viewModel.evaluationBFor(examId)
    val band = viewModel.bandFor(examId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(exam.name, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = ColorDark) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = ColorDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorWhite)
            )
        },
        containerColor = ColorGrayBg
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 総合評価セクション
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ColorWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "総合評価",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorDark
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("実績評価", fontSize = 14.sp, color = ColorDark)
                        Text(
                            text = if (evalA < 0) "--" else "${evalA}%",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeMain
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("保守的評価", fontSize = 14.sp, color = ColorDark)
                        Text(
                            text = if (evalB < 0) "--" else "${evalB}%",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeMain
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    val (bandText, bgCol, textCol) = when (band) {
                        EvaluationBand.NEEDS_WORK -> Triple("要対策", Color(0xFFFFEBEE), Color(0xFFC62828))
                        EvaluationBand.BORDER     -> Triple("ボーダー", Color(0xFFFFF3E0), Color(0xFFE65100))
                        EvaluationBand.PASS_ZONE  -> Triple("合格圏", Color(0xFFE8F5E9), Color(0xFF2E7D32))
                        EvaluationBand.UNRECORDED -> Triple("未記録", Color(0xFFF5F5F5), Color(0xFF888888))
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = bgCol,
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Text(
                            text = bandText,
                            color = textCol,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}