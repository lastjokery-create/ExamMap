package jp.maple.exammap.model

import java.time.LocalDate

enum class ExamDatePrecision {
    EXACT,
    MONTH_ONLY,
    UNDECIDED
}

enum class ExamStatus {
    PLANNED,
    ACQUIRED,
    FAILED,
    ARCHIVED
}

data class Exam(
    val id: String,
    val name: String,
    val shortName: String = "",
    val examDate: String? = null,
    val datePrecision: ExamDatePrecision = ExamDatePrecision.EXACT,
    val resultDate: String? = null,
    val status: ExamStatus = ExamStatus.PLANNED,
    val iconId: String = "school",
    val displayOrder: Int = 0,
    val statusBeforeArchive: ExamStatus? = null
) {
    fun representativeDate(): LocalDate? {
        if (examDate.isNull_orBlank()) return null
        return try {
            when (datePrecision) {
                ExamDatePrecision.EXACT -> LocalDate.parse(examDate)
                ExamDatePrecision.MONTH_ONLY -> {
                    val parts = examDate.split("-")
                    if (parts.size >= 2) {
                        LocalDate.of(parts[0].toInt(), parts[1].toInt(), 1)
                    } else null
                }
                ExamDatePrecision.UNDECIDED -> null
            }
        } catch (e: Exception) {
            null
        }
    }
}

private fun String?.isNull_orBlank(): Boolean = this == null || this.isBlank()