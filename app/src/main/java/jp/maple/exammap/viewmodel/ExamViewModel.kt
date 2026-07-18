package jp.maple.exammap.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import jp.maple.exammap.model.Exam
import jp.maple.exammap.model.ExamStatus
import jp.maple.exammap.repository.ExamRepository
import jp.maple.exammap.repository.StudyRecordRepository
import jp.maple.exammap.util.EvaluationBand
import jp.maple.exammap.util.ProgressCalculator
import kotlinx.flow.MutableStateFlow
import kotlinx.flow.StateFlow
import kotlinx.flow.asStateFlow
import kotlinx.flow.launchIn
import kotlinx.flow.onEach
import java.time.LocalDate

class ExamViewModel(application: Application) : AndroidViewModel(application) {

    private val _exams = MutableStateFlow<List<Exam>>(emptyList())
    val exams: StateFlow<List<Exam>> = _exams.asStateFlow()

    init {
        ExamRepository.init(application)
        StudyRecordRepository.init(application)

        ExamRepository.examsFlow
            .onEach { _exams.value = it }
            .launchIn(viewModelScope)
    }

    fun addExam(exam: Exam) {
        ExamRepository.addExam(exam)
    }

    fun updateExam(exam: Exam) {
        ExamRepository.updateExam(exam)
    }

    fun deleteExam(id: String) {
        ExamRepository.deleteExam(id)
    }

    fun progressFor(examId: String): Int {
        val r = StudyRecordRepository.recordsForExam(examId)
        return ProgressCalculator.calcProgress(r)
    }

    fun evaluationAFor(examId: String): Int = progressFor(examId)

    fun evaluationBFor(examId: String): Int {
        val r = StudyRecordRepository.recordsForExam(examId)
        return ProgressCalculator.calcEvaluationB(r, r.size)
    }

    fun bandFor(examId: String): EvaluationBand =
        ProgressCalculator.band(progressFor(examId))

    fun getNotificationCount(): Int {
        val today = LocalDate.now()
        return _exams.value.count { exam ->
            val repDate = exam.representativeDate()
            repDate != null && !repDate.isAfter(today) && exam.status == ExamStatus.PLANNED
        }
    }
}