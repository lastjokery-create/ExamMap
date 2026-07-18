package jp.maple.exammap.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import jp.maple.exammap.model.Exam
import kotlinx.flow.MutableStateFlow
import kotlinx.flow.StateFlow
import kotlinx.flow.asStateFlow

object ExamRepository {
    private const val PREF_NAME = "exam_map_prefs"
    private const val KEY_EXAMS = "key_exams"

    private lateinit var prefs: SharedPreferences
    private val gson: Gson = GsonBuilder().create()

    private val _examsFlow = MutableStateFlow<List<Exam>>(emptyList())
    val examsFlow: StateFlow<List<Exam>> = _examsFlow.asStateFlow()

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        loadExams()
    }

    private fun loadExams() {
        val json = prefs.getString(KEY_EXAMS, null)
        if (json.isNullOrBlank()) {
            _examsFlow.value = emptyList()
            return
        }
        try {
            val type = object : TypeToken<List<Exam>>() {}.type
            val list: List<Exam> = gson.fromJson(json, type) ?: emptyList()
            _examsFlow.value = list.sortedBy { it.displayOrder }
        } catch (e: Exception) {
            e.printStackTrace()
            _examsFlow.value = emptyList()
        }
    }

    private fun saveExams(list: List<Exam>) {
        val json = gson.toJson(list)
        prefs.edit().putString(KEY_EXAMS, json).apply()
        _examsFlow.value = list.sortedBy { it.displayOrder }
    }

    fun addExam(exam: Exam) {
        val current = _examsFlow.value.toMutableList()
        current.add(exam)
        saveExams(current)
    }

    fun updateExam(exam: Exam) {
        val current = _examsFlow.value.toMutableList()
        val index = current.indexOfFirst { it.id == exam.id }
        if (index != -1) {
            current[index] = exam
            saveExams(current)
        }
    }

    fun deleteExam(id: String) {
        val current = _examsFlow.value.filterNot { it.id == id }
        saveExams(current)
    }

    fun getExamById(id: String): Exam? {
        return _examsFlow.value.find { it.id == id }
    }
}