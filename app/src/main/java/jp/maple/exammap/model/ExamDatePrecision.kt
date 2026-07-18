package jp.maple.exammap.model

enum class ExamDatePrecision(
    val displayName: String
) {
    EXACT("正式日"),
    MONTH("月のみ"),
    EARLY_MONTH("上旬"),
    MID_MONTH("中旬"),
    LATE_MONTH("下旬"),
    UNDECIDED("未定")
}