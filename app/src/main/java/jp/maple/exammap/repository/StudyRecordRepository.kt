package jp.maple.exammap.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import jp.maple.exammap.model.StudyRecord
import jp.maple.exammap.util.LocalDateTimeAdapter
import kotlinx.flow.MutableStateFlow
import kotlinx.flow.StateFlow
import kotlinx.flow.asStateFlow
import java.time.LocalDateTime

object StudyRecordRepository {
    private const val PREF_NAME = "study_record_prefs"
    private const val KEY_RECORDS = "key_study_records"

    private lateinit var prefs: SharedPreferences
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    private val _recordsFlow = MutableStateFlow<List<StudyRecord>>(emptyList())
    val recordsFlow: StateFlow<List<StudyRecord>> = _recordsFlow.asStateFlow()

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        loadRecords()
    }

    private fun loadRecords() {
        val json = prefs.getString(KEY_RECORDS, null)
        if (json.isNullOrBlank()) {
            _recordsFlow.value = emptyList()
            return
        }
        try {
            val type = object : TypeToken<List<StudyRecord>>() {}.type
            val list: List<StudyRecord> = gson.fromJson(json, type) ?: emptyList()
            _recordsFlow.value = list
        } catch (e: Exception) {
            e.printStackTrace()
            _recordsFlow.value = emptyList()
        }
    }

    private fun saveRecords(list: List<StudyRecord>) {
        val json = gson.toJson(list)
        prefs.edit().putString(KEY_RECORDS, json).apply()
        _recordsFlow.value = list
    }

    fun addRecord(record: StudyRecord) {
        val current = _recordsFlow.value.toMutableList()
        current.add(record)
        saveRecords(current)
    }

    fun updateRecord(record: StudyRecord) {
        val current = _recordsFlow.value.toMutableList()
        val index = current.indexOfFirst { it.id == record.id }
        if (index != -1) {
            current[index] = record
            saveRecords(current)
        }
    }

    fun deleteRecord(id: String) {
        val current = _recordsFlow.value.filterNot { it.id == id }
        saveRecords(current)
    }

    fun recordsForExam(examId: String): List<StudyRecord> {
        return _recordsFlow.value.filter { it.examId == examId }
    }

    fun getRecordById(id: String): StudyRecord? {
        return _recordsFlow.value.find { it.id == id }
    }
}