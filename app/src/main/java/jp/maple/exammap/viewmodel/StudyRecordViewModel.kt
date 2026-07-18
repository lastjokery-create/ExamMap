package jp.maple.exammap.viewmodel

import androidx.lifecycle.ViewModel
import jp.maple.exammap.model.StudyRecord
import jp.maple.exammap.repository.StudyRecordRepository
import java.util.UUID

class StudyRecordViewModel : ViewModel() {
    val records: List<StudyRecord>
        get() = StudyRecordRepository.records

    fun addRecord(
        examId: String,
        pastExamId: String,
        studyDate: String,
        score: Int,
        maxScore: Int,
        passed: Boolean,
        memo: String
    ) {
        if (examId.isBlank() || studyDate.isBlank() || maxScore <= 0) return

        StudyRecordRepository.addRecord(
            StudyRecord(
                id = UUID.randomUUID().toString(),
                examId = examId,
                pastExamId = pastExamId.trim(),
                studyDate = studyDate.trim(),
                score = score.coerceIn(0, maxScore),
                maxScore = maxScore,
                passed = passed,
                memo = memo.trim()
            )
        )
    }

    fun updateRecord(
        original: StudyRecord,
        pastExamId: String,
        studyDate: String,
        score: Int,
        maxScore: Int,
        passed: Boolean,
        memo: String
    ) {
        if (studyDate.isBlank() || maxScore <= 0) return

        StudyRecordRepository.updateRecord(
            original.copy(
                pastExamId = pastExamId.trim(),
                studyDate = studyDate.trim(),
                score = score.coerceIn(0, maxScore),
                maxScore = maxScore,
                passed = passed,
                memo = memo.trim()
            )
        )
    }

    fun findRecordById(recordId: String): StudyRecord? =
        StudyRecordRepository.findById(recordId)

    fun deleteRecord(record: StudyRecord) {
        StudyRecordRepository.removeRecord(record)
    }
}
