package jp.maple.exammap.repository

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import jp.maple.exammap.model.Exam
import jp.maple.exammap.model.ExamDatePrecision
import jp.maple.exammap.model.ExamStatus
import org.json.JSONArray
import org.json.JSONObject

object ExamRepository {

    private const val PREFS_NAME = "exam_map_prefs"
    private const val EXAMS_KEY = "exams"

    val exams = mutableStateListOf<Exam>()
    private var appContext: Context? = null

    fun initialize(context: Context) {
        if (appContext != null) return
        appContext = context.applicationContext
        loadExams()
    }

    fun addExam(exam: Exam): Boolean {
        if (hasDuplicateName(exam.name)) return false
        exams.add(exam.copy(displayOrder = nextDisplayOrder()))
        saveExams()
        return true
    }

    fun findExamById(id: String): Exam? = exams.find { it.id == id }

    fun updateExam(updatedExam: Exam): Boolean {
        val index = exams.indexOfFirst { it.id == updatedExam.id }
        if (index == -1 || hasDuplicateName(updatedExam.name, updatedExam.id)) return false
        exams[index] = updatedExam
        saveExams()
        return true
    }

    fun archiveExam(examId: String) {
        val exam = findExamById(examId) ?: return
        if (exam.status == ExamStatus.ARCHIVED) return
        updateExam(
            exam.copy(
                statusBeforeArchive = exam.status,
                status = ExamStatus.ARCHIVED
            )
        )
    }

    fun restoreExam(examId: String) {
        val exam = findExamById(examId) ?: return
        if (exam.status != ExamStatus.ARCHIVED) return
        updateExam(
            exam.copy(
                status = exam.statusBeforeArchive ?: ExamStatus.PLANNED,
                statusBeforeArchive = null
            )
        )
    }

    private fun hasDuplicateName(name: String, excludingId: String? = null): Boolean {
        val normalized = name.trim()
        return exams.any {
            it.id != excludingId && it.name.trim().equals(normalized, ignoreCase = true)
        }
    }

    private fun nextDisplayOrder(): Int = (exams.maxOfOrNull { it.displayOrder } ?: -1) + 1

    private fun saveExams() {
        val context = appContext ?: return
        val jsonArray = JSONArray()
        exams.forEach { exam ->
            jsonArray.put(JSONObject().apply {
                put("id", exam.id)
                put("name", exam.name)
                put("shortName", exam.shortName)
                put("examDate", exam.examDate)
                put("datePrecision", exam.datePrecision.name)
                put("resultDate", exam.resultDate)
                put("status", exam.status.name)
                put("progress", exam.progress)
                put("iconId", exam.iconId)
                put("displayOrder", exam.displayOrder)
                put("statusBeforeArchive", exam.statusBeforeArchive?.name ?: JSONObject.NULL)
            })
        }
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(EXAMS_KEY, jsonArray.toString())
            .apply()
    }

    private fun loadExams() {
        val context = appContext ?: return
        val jsonText = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(EXAMS_KEY, null) ?: return

        try {
            val jsonArray = JSONArray(jsonText)
            exams.clear()
            for (index in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(index)
                val exam = Exam(
                    id = item.optString("id", ""),
                    name = item.optString("name", ""),
                    shortName = item.optString("shortName", ""),
                    examDate = item.optString("examDate", ""),
                    datePrecision = parseDatePrecision(item.optString("datePrecision", ExamDatePrecision.UNDECIDED.name)),
                    resultDate = item.optString("resultDate", ""),
                    status = parseExamStatus(item.optString("status", ExamStatus.PLANNED.name)),
                    progress = item.optInt("progress", 0).coerceIn(0, 100),
                    iconId = item.optString("iconId", "default_exam"),
                    displayOrder = item.optInt("displayOrder", index),
                    statusBeforeArchive = item.optString("statusBeforeArchive", "")
                        .takeIf { it.isNotBlank() && it != "null" }
                        ?.let(::parseExamStatus)
                )
                if (exam.id.isNotBlank()) exams.add(exam)
            }
        } catch (_: Exception) {
            exams.clear()
        }
    }

    private fun parseDatePrecision(value: String): ExamDatePrecision =
        runCatching { ExamDatePrecision.valueOf(value) }.getOrDefault(ExamDatePrecision.UNDECIDED)

    /** Migrates status names used by earlier source ZIPs. */
    private fun parseExamStatus(value: String): ExamStatus = when (value) {
        "PASSED" -> ExamStatus.PASSED
        "FAILED" -> ExamStatus.FAILED
        "ARCHIVED" -> ExamStatus.ARCHIVED
        "NOT_STARTED", "STUDYING", "TAKEN", "PLANNED" -> ExamStatus.PLANNED
        else -> ExamStatus.PLANNED
    }
}
