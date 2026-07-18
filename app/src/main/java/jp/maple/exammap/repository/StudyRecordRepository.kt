package jp.maple.exammap.repository

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import jp.maple.exammap.model.StudyRecord
import org.json.JSONArray
import org.json.JSONObject

object StudyRecordRepository {
    private const val PREFS_NAME = "exam_map_prefs"
    private const val RECORDS_KEY = "study_records"

    val records = mutableStateListOf<StudyRecord>()

    private var appContext: Context? = null

    fun initialize(context: Context) {
        if (appContext != null) return
        appContext = context.applicationContext
        loadRecords()
    }

    fun addRecord(record: StudyRecord) {
        records.add(record)
        saveRecords()
    }

    fun updateRecord(record: StudyRecord) {
        val index = records.indexOfFirst { it.id == record.id }
        if (index < 0) return
        records[index] = record
        saveRecords()
    }

    fun removeRecord(record: StudyRecord) {
        records.removeAll { it.id == record.id }
        saveRecords()
    }

    fun recordsForExam(examId: String): List<StudyRecord> =
        records.filter { it.examId == examId }

    fun findById(recordId: String): StudyRecord? =
        records.firstOrNull { it.id == recordId }

    private fun saveRecords() {
        val context = appContext ?: return
        val jsonArray = JSONArray()

        records.forEach { record ->
            jsonArray.put(
                JSONObject().apply {
                    put("id", record.id)
                    put("examId", record.examId)
                    put("pastExamId", record.pastExamId)
                    put("studyDate", record.studyDate)
                    put("score", record.score)
                    put("maxScore", record.maxScore)
                    put("passed", record.passed)
                    put("memo", record.memo)
                }
            )
        }

        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(RECORDS_KEY, jsonArray.toString())
            .apply()
    }

    private fun loadRecords() {
        val context = appContext ?: return
        val jsonText = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(RECORDS_KEY, null) ?: return

        try {
            val jsonArray = JSONArray(jsonText)
            records.clear()

            for (index in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(index)
                val record = StudyRecord(
                    id = item.optString("id"),
                    examId = item.optString("examId"),
                    pastExamId = item.optString("pastExamId"),
                    studyDate = item.optString("studyDate"),
                    score = item.optInt("score", 0),
                    maxScore = item.optInt("maxScore", 0),
                    passed = item.optBoolean("passed", false),
                    memo = item.optString("memo")
                )
                if (record.id.isNotBlank() && record.examId.isNotBlank()) records.add(record)
            }
        } catch (_: Exception) {
            records.clear()
        }
    }
}
