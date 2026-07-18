package jp.maple.exammap.model

import java.time.LocalDateTime

data class StudyRecord(
    val id: String,
    val examId: String,
    val date: LocalDateTime,
    val score: Int = 0,
    val maxScore: Int = 100,
    val note: String = "",
    val includedInAggregate: Boolean = true
) {
    val percentage: Int
        get() = if (maxScore <= 0) 0
        else (score.toDouble() / maxScore * 100).toInt().coerceIn(0, 100)
}