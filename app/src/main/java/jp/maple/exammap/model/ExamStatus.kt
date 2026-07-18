package jp.maple.exammap.model

enum class ExamStatus(
    val displayName: String
) {
    PLANNED("受験予定"),
    PASSED("合格"),
    FAILED("不合格"),
    ARCHIVED("アーカイブ")
}
