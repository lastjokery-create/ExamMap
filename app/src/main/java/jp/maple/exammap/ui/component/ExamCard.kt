package jp.maple.exammap.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.maple.exammap.model.Exam
import jp.maple.exammap.model.ExamDatePrecision
import jp.maple.exammap.ui.theme.ColorBorder
import jp.maple.exammap.ui.theme.ColorDark
import jp.maple.exammap.ui.theme.ColorGrayText
import jp.maple.exammap.ui.theme.ColorWhite
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun ExamCard(
    exam: Exam,
    accentColor: Color,
    accentBadgeBg: Color,
    progress: Int,
    onCardClick: () -> Unit
) {
    val today = LocalDate.now()
    val (badgeText, badgeBgColor, badgeTextColor) = badgeInfo(exam, today, accentColor, accentBadgeBg)
    val displayName = exam.shortName.ifBlank { exam.name }
    val dateText = exam.examDate ?: "未定"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ColorWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(accentColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = examIcon(exam.iconId),
                        contentDescription = null,
                        tint = ColorWhite
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = displayName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = ColorDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = ColorGrayText
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = dateText,
                            fontSize = 13.sp,
                            color = ColorGrayText
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = badgeBgColor
                    ) {
                        Text(
                            text = badgeText,
                            color = badgeTextColor,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "進捗",
                        fontSize = 11.sp,
                        color = ColorGrayText
                    )
                    val progressText = if (progress < 0) "--" else "${progress}%"
                    Text(
                        text = progressText,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (progress < 0) ColorGrayText else accentColor
                    )
                    if (progress >= 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { progress / 100f },
                            color = accentColor,
                            trackColor = ColorBorder,
                            modifier = Modifier
                                .width(72.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )
                    }
                }
            }
        }
    }
}

fun examIcon(iconId: String): ImageVector = when (iconId) {
    "factory"     -> Icons.Default.Factory
    "code"        -> Icons.Default.Code
    "settings"    -> Icons.Default.Settings
    "book"        -> Icons.Default.MenuBook
    "bolt"        -> Icons.Default.Bolt
    "water_drop"  -> Icons.Default.WaterDrop
    "list"        -> Icons.Default.Assignment
    "hard_hat"    -> Icons.Default.Engineering
    "certificate" -> Icons.Default.CardMembership
    else          -> Icons.Default.School
}

fun badgeInfo(exam: Exam, today: LocalDate, accentColor: Color, accentBadgeBg: Color): Triple<String, Color, Color> {
    return when (exam.datePrecision) {
        ExamDatePrecision.UNDECIDED ->
            Triple("未定", Color(0xFFFFF3E0), Color(0xFFFF8A00))
        ExamDatePrecision.MONTH_ONLY ->
            Triple("予定", Color(0xFFF5F5F5), Color(0xFF888888))
        else -> {
            val rep = exam.representativeDate()
            if (rep == null) Triple("予定", Color(0xFFF5F5F5), Color(0xFF888888))
            else {
                val days = ChronoUnit.DAYS.between(today, rep).coerceAtLeast(0)
                Triple("あと${days}日", accentBadgeBg, accentColor)
            }
        }
    }
}