package jp.maple.exammap.model

data class StudyRecord(
    val id: String,
    val examId: String,
    val pastExamId: String,
    val studyDate: String,
    val score: Int,
    val maxScore: Int,
    val passed: Boolean,
    val memo: String = ""
) {
    val percentage: Int
        get() = if (maxScore <= 0) 0 else ((score.toDouble() / maxScore) * 100).toInt().coerceIn(0, 100)
}
