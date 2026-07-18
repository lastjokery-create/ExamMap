package jp.maple.exammap.model

data class Exam(
    val id: String,
    val name: String,
    val shortName: String = "",
    val examDate: String = "",
    val datePrecision: ExamDatePrecision = ExamDatePrecision.UNDECIDED,
    val resultDate: String = "",
    val status: ExamStatus = ExamStatus.PLANNED,
    val progress: Int = 0,
    val iconId: String = "default_exam",
    val displayOrder: Int = 0,
    val statusBeforeArchive: ExamStatus? = null
)
