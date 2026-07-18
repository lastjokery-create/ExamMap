package jp.maple.exammap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import jp.maple.exammap.repository.ExamRepository
import jp.maple.exammap.repository.StudyRecordRepository
import jp.maple.exammap.ui.navigation.AppNavigation
import jp.maple.exammap.ui.theme.ExamMapTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        ExamRepository.initialize(
            applicationContext
        )

        StudyRecordRepository.initialize(
            applicationContext
        )

        setContent {
            ExamMapTheme {
                AppNavigation()
            }
        }
    }
}