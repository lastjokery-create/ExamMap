package jp.maple.exammap.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Exams : Screen("exams")
    data object Records : Screen("records")
    data object Settings : Screen("settings")
    data object AddExam : Screen("add_exam")
    data object AcquiredQualifications : Screen("acquired_qualifications")
    data object Archive : Screen("archive")

    data object ExamDetail : Screen("exam_detail/{examId}") {
        fun createRoute(examId: String): String = "exam_detail/$examId"
    }

    data object AddRecord : Screen("add_record/{examId}") {
        fun createRoute(examId: String): String = "add_record/$examId"
    }

    data object EditRecord : Screen("edit_record/{recordId}") {
        fun createRoute(recordId: String): String = "edit_record/$recordId"
    }
}
