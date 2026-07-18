package jp.maple.exammap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.maple.exammap.ui.navigation.AppNavigation
import jp.maple.exammap.ui.theme.ExamMapTheme
import jp.maple.exammap.ui.viewmodel.ExamViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExamMapTheme {
                val examViewModel: ExamViewModel = viewModel()
                AppNavigation(examViewModel = examViewModel)
            }
        }
    }
}