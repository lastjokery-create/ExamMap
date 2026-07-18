package jp.maple.exammap.viewmodel

import androidx.lifecycle.ViewModel
import jp.maple.exammap.model.Exam
import jp.maple.exammap.model.ExamDatePrecision
import jp.maple.exammap.model.ExamStatus
import jp.maple.exammap.repository.ExamRepository
import java.util.UUID

class ExamViewModel : ViewModel() {
    val exams: List<Exam> get() = ExamRepository.exams

    fun addExam(
        name: String,
        shortName: String,
        examDate: String,
        datePrecision: ExamDatePrecision
    ): Boolean {
        val trimmedName = name.trim()
        if (trimmedName.isBlank()) return false
        return ExamRepository.addExam(
            Exam(
                id = UUID.randomUUID().toString(),
                name = trimmedName,
                shortName = shortName.trim(),
                examDate = examDate.trim(),
                datePrecision = datePrecision,
                status = ExamStatus.PLANNED
            )
        )
    }

    fun updateExam(exam: Exam): Boolean {
        if (exam.name.isBlank()) return false
        return ExamRepository.updateExam(
            exam.copy(
                name = exam.name.trim(),
                shortName = exam.shortName.trim(),
                examDate = exam.examDate.trim(),
                resultDate = exam.resultDate.trim(),
                progress = exam.progress.coerceIn(0, 100)
            )
        )
    }

    fun setExamResult(exam: Exam, status: ExamStatus): Boolean {
        if (status != ExamStatus.PASSED && status != ExamStatus.FAILED) return false
        return updateExam(exam.copy(status = status))
    }

    fun archiveExam(exam: Exam) = ExamRepository.archiveExam(exam.id)
    fun restoreExam(exam: Exam) = ExamRepository.restoreExam(exam.id)
    fun findExamById(id: String): Exam? = ExamRepository.findExamById(id)
}
