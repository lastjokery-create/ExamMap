package jp.maple.exammap.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import jp.maple.exammap.model.StudyRecord
import jp.maple.exammap.repository.StudyRecordRepository
import kotlinx.flow.StateFlow

class StudyRecordViewModel(application: Application) : AndroidViewModel(application) {

    val records: StateFlow<List<StudyRecord>> = StudyRecordRepository.recordsFlow

    init {
        StudyRecordRepository.init(application)
    }

    fun addRecord(record: StudyRecord) {
        StudyRecordRepository.addRecord(record)
    }

    fun updateRecord(record: StudyRecord) {
        StudyRecordRepository.updateRecord(record)
    }

    fun deleteRecord(id: String) {
        StudyRecordRepository.deleteRecord(id)
    }

    fun recordsForExam(examId: String): List<StudyRecord> {
        return StudyRecordRepository.recordsForExam(examId)
    }

    fun getRecordById(id: String): StudyRecord? {
        return StudyRecordRepository.getRecordById(id)
    }
}